package com.filozilla.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.filozilla.R
import com.filozilla.databinding.FragmentDailyDtPickerBinding
import com.filozilla.interfaces.CommonInterface
import com.filozilla.models.DailyDatePicker
import com.filozilla.repositories.CustomDialogRepo
import com.filozilla.repositories.DailyDtRepo
import com.filozilla.repositories.ViewChangesRepo
import com.filozilla.viewmodels.DailyDTPickerVM
import java.util.concurrent.atomic.AtomicInteger

@SuppressLint("SetTextI18n", "SimpleDateFormat")
class DailyDTPickerFragment: Fragment(), CommonInterface {
    private lateinit var binding: FragmentDailyDtPickerBinding
    private lateinit var viewModel: DailyDTPickerVM
    private lateinit var dailyDtRepo: DailyDtRepo
    private lateinit var viewChangesRepo: ViewChangesRepo
    private lateinit var dateList: ArrayList<DailyDatePicker>
    private var viewList: List<View>? = null
    private var pickDateController = AtomicInteger(-1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempVM: DailyDTPickerVM by viewModels()
        this.viewModel = tempVM
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDailyDtPickerBinding.inflate(inflater)
        binding.apply {
            dtPickerObject = this@DailyDTPickerFragment
            backMonthAlpha = 0.5f
            nextMonthAlpha = 1.0f
            pickUpTime = "12:00"
            dropOffTime = "12:00"
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeComponents()
    }

    fun backOnClick() = Navigation.findNavController(binding.root).navigate(R.id.dtPickerToHomePage)

    fun infoOnClick() = CustomDialogRepo(context = requireContext()).showSuccessDialog(controller = true, msg = getString(R.string.daily_date_range_info))

    fun confirmOnClick() = viewModel.getAndCompareDates(context = requireContext(), view = binding.btnTimePickerConfirm, pickUpTime = binding.pickUpTime!!, dropOffTime = binding.dropOffTime!!)

    fun dayOnClick(tv: TextView) {
        val controller = tv.currentTextColor == ContextCompat.getColor(requireContext(), R.color.text_color)
        val controller2 = tv.currentTextColor == ContextCompat.getColor(requireContext(), R.color.white)
        val value = tv.text.toString()

        if (controller) {
            daySelectionController(tv = tv)
            customizeDayRangeBg()
        }else if (controller2) {
            removeDayRangeBg()
            removeDayBg(tv = tv)
            removeDaySelection(tv = tv)
        }else {
            if (value.isEmpty())
                return
            
            showToastMsg(msg = getString(R.string.day_click_error))
        }
    }

    override fun initializeComponents() {
        initRepos()
        initDates()
        initViews()
    }

    private fun initRepos() {
        dailyDtRepo = DailyDtRepo(context = requireContext())
        viewChangesRepo = ViewChangesRepo(activity = requireActivity(), context = requireContext())
    }

    private fun initDates() {
        dateList = getDateList()
    }

    private fun initViews() {
        viewList = listOf(binding.tvDt, binding.tvDt2, binding.tvDt3, binding.tvDt4, binding.tvDt5, binding.tvDt6, binding.tvDt7, binding.tvDt8, binding.tvDt9, binding.tvDt10, binding.tvDt11, binding.tvDt12, binding.tvDt13, binding.tvDt14, binding.tvDt15, binding.tvDt16, binding.tvDt17, binding.tvDt18, binding.tvDt19, binding.tvDt20, binding.tvDt21, binding.tvDt22, binding.tvDt23, binding.tvDt24, binding.tvDt25, binding.tvDt26, binding.tvDt27, binding.tvDt28, binding.tvDt29, binding.tvDt30, binding.tvDt31, binding.tvDt32, binding.tvDt33, binding.tvDt34, binding.tvDt35, binding.tvDt36, binding.tvDt37)

        getDates(isIncrease = true)

        binding.apply {
            sbTpPickUp.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                    binding.pickUpTime = viewModel.timeList[progress]
                }
                override fun onStartTrackingTouch(p0: SeekBar?) {}
                override fun onStopTrackingTouch(p0: SeekBar?) {}
            })

            sbTpDropOff.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                    binding.dropOffTime = viewModel.timeList[progress]
                }
                override fun onStartTrackingTouch(p0: SeekBar?) {}
                override fun onStopTrackingTouch(p0: SeekBar?) {}
            })
        }
    }

    override fun touchListeners() {

    }

    fun getDates(isIncrease: Boolean) {
        val backController = binding.backMonthAlpha == 1.0f
        val nextController = binding.nextMonthAlpha == 1.0f

        if (isIncrease) {
            if (nextController) {
                pickDateController.incrementAndGet()
                getDate()
            }else
                pickDateController.set(12)
        }else {
            if (backController) {
                pickDateController.decrementAndGet()
                getDate()
            }else
                pickDateController.set(0)
        }

        customizeDayRangeBg()
    }

    private fun getDate() {
        if (pickDateController.get() in 0 ..  12) {
            binding.tvDailyDateMonthYear.text = "${getMonthName(position = dateList[pickDateController.get()].month)} ${dateList[pickDateController.get()].year}"
            getDays(count = pickDateController.get())
        }
    }

    private fun getDays(count: Int) {
        for (index in 0 until 37) {
            viewList?.let { list ->
                val tv = list[index] as TextView
                val value = dateList[count].days[index]
                tv.text = value

                daysUiController(count = count, tv = tv, value = value)
            }
        }

        navUiController()
    }

    private fun daySelectionController(tv: TextView) {
        val value = tv.text.toString().toIntOrNull()
        val monthAndYear = binding.tvDailyDateMonthYear.text.toString()

        value?.let {
            // Aynı ay için 2'den fazla seçim yapınca ui temizlemektedir.
            viewModel.getSelection().run {
                if (this.chosenFirstDay != 0 && this.chosenLastDay != 0) {
                    removeSelection()

                    if (this.chosenFirstPos == this.chosenLastPos) {
                        getDays(count = pickDateController.get())
                    }

                }
            }

            when(viewModel.getDayClick().incrementAndGet() % 2) {
                0 -> {
                    // Son tarih seçimini ilk kez gerçekleştirmektedir. Sonrasında bu blok çalışmamaktadır.
                    viewModel.setLast(chosenLastPos = pickDateController.get(), chosenLastDay = it, chosenLastDate = "$it $monthAndYear")
                }
                1 -> {
                    // İlk tarih seçimini ilk kez gerçekleştirmektedir. Sonrasında bu blok çalışmamaktadır.
                    viewModel.setFirst(chosenFirstPos = pickDateController.get(), chosenFirstDay = it, chosenFirstDate = "$it $monthAndYear")
                }
            }

            dayUiController(tv = tv)
        }?: showToastMsg(msg = getString(R.string.day_click_error))
    }

    private fun dayUiController(tv: TextView) {
        when(viewModel.getDayClick().get() % 2) {
            0 -> {
                customizeSpecificDayRangeBg(tv = tv)
            }
            1 -> {
                customizeDayBg(tv = tv)
            }
        }
    }

    private fun daysUiController(count: Int, tv: TextView, value: String) {
        if ((value == "-" || value.isEmpty())) {
            tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.hint_color))
            tv.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background))
        }else if ((value.toInt() < getDayOfToday()) && (pickDateController.get() == 0)) {
            tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.hint_color))
            tv.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background))
        }else if ((value.toInt() > getDayOfToday()) && (pickDateController.get() == 12)) {
            tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.hint_color))
            tv.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background))
        }else {
            tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
            tv.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background))
        }

        viewModel.getSelection().run {
            if ((value != "-") and (value.isNotEmpty())) {
                if ((viewModel.getDayClick().get() != 0) and (this.chosenFirstPos == count) and (this.chosenFirstDay == value.toInt())) {
                    customizeSingleDayBg(tv = tv)
                }
                if ((viewModel.getDayClick().get() != 0) and (this.chosenLastPos == count) and (this.chosenLastDay == value.toInt())) {
                    customizeSingleDayBg(tv = tv)
                }
            }
        }
    }

    private fun customizeDayBg(tv: TextView) {
        customizeSingleDayBg(tv = tv)
    }

    // Bu metotta son tarih seçimi yapıldığında sadece ilgili pozisyon için aralığı boyama kodlaması yapılmıştır.
    private fun customizeSpecificDayRangeBg(tv: TextView) {
        viewModel.getSelection().run {
            if ((pickDateController.get() == this.chosenFirstPos) and (pickDateController.get() == this.chosenLastPos)) {
                viewList?.forEach { view ->
                    val tView = view as TextView
                    val value = tView.text.toString().toIntOrNull()

                    value?.let {
                        if ((it in this.chosenFirstDay + 1 until this.chosenLastDay) or (it in this.chosenLastDay + 1 until this.chosenFirstDay)) {
                            changeTvBg3(tv = tView)
                        }
                    }
                }
            }
        }

        customizeSingleDayBg(tv = tv)
    }

    private fun customizeDayRangeBg() {
        viewModel.getSelection().run {
            if ((this.chosenFirstDay != 0) and (this.chosenLastDay != 0)) {

                // Eğer ilk ve son günler aynı pozisyon içerisinden seçildiyse bu koşul çalışır.
                if ((pickDateController.get() == this.chosenFirstPos) and (this.chosenFirstPos == this.chosenLastPos)) {
                    viewList?.forEach { tView ->
                        val tv = tView as TextView
                        val value = tv.text.toString().toIntOrNull()
                        val controller = tView.background == ContextCompat.getDrawable(requireContext(), R.drawable.dt_day_bg3)

                        value?.let {
                            if (!controller) {
                                if ((it in this.chosenFirstDay + 1 until this.chosenLastDay) or (it in this.chosenLastDay + 1 until this.chosenFirstDay)) {
                                    changeTvBg3(tv = tv)
                                }
                            }
                        }

                    }
                }

                // Kullanıcının seçtiği son gün, seçtiği ilk günden pozisyon olarak büyükse bu koşul altındaki koşullar çalışır.
                if (this.chosenLastPos > this.chosenFirstPos) {

                    // Kullanıcı geri/sonraki butonlarına tıkladığında ilk seçilen güne ait pozisyona gelince bu koşul çalışır ve seçilen günden sonraki günler tamamen boyanır.
                    if (pickDateController.get() == this.chosenFirstPos) {
                        viewList?.forEach { tView ->
                            val tv = tView as TextView
                            val value = tv.text.toString().toIntOrNull()

                            value?.let {
                                if (it > this.chosenFirstDay) {
                                    changeTvBg3(tv = tv)
                                }
                            }
                        }
                    }

                    // Kullanıcı geri/ileri butonlarına tıkladığında son seçilen güne ait pozisyona gelince bu koşul çalışır ve seçilen günden önceki günler tamamen boyanır.
                    if (pickDateController.get() == this.chosenLastPos) {
                        viewList?.forEach { tView ->
                            val tv = tView as TextView
                            val value = tv.text.toString().toIntOrNull()

                            value?.let {
                                if (it < this.chosenLastDay) {
                                    changeTvBg3(tv = tv)
                                }
                            }
                        }
                    }

                    // Kullanıcı geri/ileri butonlarına tıkladığında ilk ve son seçilen pozisyon aralığındaysa bu koşul çalışır ve içerik tamamen boyanır.
                    if (pickDateController.get() in this.chosenFirstPos + 1 until this.chosenLastPos) {
                        viewList?.forEach { tView ->
                            val tv = tView as TextView
                            val value = tv.text.toString().toIntOrNull()

                            value?.let {// Aşağıdaki tarzda kodlamalar tek tek metot içerisine alınacak ve metottan çağırılacak.
                                changeTvBg3(tv = tv)
                            }
                        }
                    }
                }

                // Kullanıcının seçtiği ilk gün, seçtiği son günden pozisyon olarak büyükse bu koşul altındaki koşullar çalışır.
                if (this.chosenLastPos < this.chosenFirstPos) {
                    if (pickDateController.get() == this.chosenFirstPos) {
                        viewList?.forEach { tView ->
                            val tv = tView as TextView
                            val value = tv.text.toString().toIntOrNull()

                            value?.let {
                                if (it < this.chosenFirstDay) {
                                    changeTvBg3(tv = tv)
                                }
                            }
                        }
                    }

                    // Kullanıcı geri/ileri butonlarına tıkladığında son seçilen güne ait pozisyona gelince bu koşul çalışır ve seçilen günden sonraki günler tamamen boyanır.
                    if (pickDateController.get() == this.chosenLastPos) {
                        viewList?.forEach { tView ->
                            val tv = tView as TextView
                            val value = tv.text.toString().toIntOrNull()

                            value?.let {
                                if (it > this.chosenLastDay) {
                                    changeTvBg3(tv = tv)
                                }
                            }
                        }
                    }

                    // Kullanıcı geri/ileri butonlarına tıkladığında ilk ve son seçilen pozisyon aralığındaysa bu koşul çalışır ve içerik tamamen boyanır.
                    if (pickDateController.get() in this.chosenLastPos + 1 until this.chosenFirstPos) {
                        viewList?.forEach { tView ->
                            val tv = tView as TextView
                            val value = tv.text.toString().toIntOrNull()

                            value?.let {// Aşağıdaki tarzda kodlamalar tek tek metot içerisine alınacak ve metottan çağırılacak.
                                changeTvBg3(tv = tv)
                            }
                        }
                    }
                }

            }
        }
    }

    private fun changeTvBg3(tv: TextView) {
        tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
        tv.background = ContextCompat.getDrawable(requireContext(), R.drawable.dt_day_bg3)
    }

    private fun customizeSingleDayBg(tv: TextView) {
        tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        tv.background = ContextCompat.getDrawable(requireContext(), R.drawable.dt_day_bg)
    }

    private fun removeDayBg(tv: TextView) {
        removeSingleDayBg(tv = tv)
    }

    private fun removeSingleDayBg(tv: TextView) {
        tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
        tv.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background))
    }

    private fun removeDayRangeBg() {
        viewModel.getSelection().run {
            if ((this.chosenFirstDay != 0 && this.chosenLastDay != 0)) {
                if ((pickDateController.get() == this.chosenFirstPos) and (pickDateController.get() == this.chosenLastPos)) {
                    viewList?.forEach { view ->
                        val tView = view as TextView
                        val value = tView.text.toString().toIntOrNull()

                        value?.let {
                            if ((it in this.chosenFirstDay + 1 until this.chosenLastDay) or (it in this.chosenLastDay + 1 until this.chosenFirstDay)) {
                                tView.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
                                tView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background))
                            }
                        }
                    }
                }else if(chosenFirstPos > chosenLastPos) {
                    // İlk pozisyon son pozisyondan büyükse, ilk pozisyondaki seçilen güne tıklanıldığında range'i silme kodu yazılmıştır.
                    if (pickDateController.get() == chosenFirstPos) {
                        viewList?.forEach { tView ->
                            val tv = tView as TextView
                            val value = tv.text.toString().toIntOrNull()

                            value?.let {
                                if (it < this.chosenFirstDay) {
                                    tView.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
                                    tView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background))
                                }
                            }
                        }
                    }else if (pickDateController.get() == chosenLastPos) {
                        viewList?.forEach { tView ->
                            val tv = tView as TextView
                            val value = tv.text.toString().toIntOrNull()

                            value?.let {
                                if (it > this.chosenLastDay) {
                                    tView.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
                                    tView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background))
                                }
                            }
                        }
                    }
                }else if (pickDateController.get() == this.chosenFirstPos) {
                    viewList?.forEach { tView ->
                        val tv = tView as TextView
                        val value = tv.text.toString().toIntOrNull()

                        value?.let {
                            if (it >= this.chosenFirstDay) {
                                tView.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
                                tView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background))
                            }
                        }
                    }
                }else if (pickDateController.get() == this.chosenLastPos) {
                    viewList?.forEach { tView ->
                        val tv = tView as TextView
                        val value = tv.text.toString().toIntOrNull()

                        value?.let {
                            if (it <= this.chosenLastDay) {
                                tView.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
                                tView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background))
                            }
                        }
                    }
                }
            }
        }
    }

    private fun removeDaySelection(tv: TextView) = viewModel.removeDaySelection(pos = pickDateController.get(), value = tv.text.toString().toIntOrNull())

    // Bu metotta 2'den fazla tarih seçimi yapılırsa geçmiş seçimlerin pozisyonlarını silecek kod yazılmıştır.
    private fun removeSelection() = viewModel.removeSelection()

    // Bu metotta ileri/geri tuşlarına basılarak tarih değiştirirken başa veya sona gelince tıklanılmayı devre dışı bırakacak kontrol kodlaması yapılmıştır.
    private fun navUiController() {
        if (pickDateController.get() >= 0) {
            if (pickDateController.get() == 0) {
                binding.backMonthAlpha = 0.5f
            }else {
                binding.backMonthAlpha = 1.0f
            }
        }
        if (pickDateController.get() <= 12) {
            if (pickDateController.get() == 12) {
                binding.nextMonthAlpha = 0.5f
            }else {
                binding.nextMonthAlpha = 1.0f
            }
        }
    }

    private fun getDateList(): ArrayList<DailyDatePicker> {
        val dates = ArrayList<DailyDatePicker>()
        var index = 0

        while (index <= 12) {
            var dayIndex = 0
            val days = ArrayList<String>()
            val month = getMonthList()[index]
            val year = getYearList()[index]

            for (pos in 0 until 37) {
                if (pos in getFirstDayList()[index] until getEndDayList()[index] + getFirstDayList()[index])
                    days.add(String.format("%02d", ++dayIndex))
                else {
                    if (pos < 35)
                        days.add("-")
                    else
                        days.add("")
                }
            }

            dates.add(
                DailyDatePicker(
                    days = days,
                    month = month,
                    year = year
                )
            )

            index++
        }

        return dates
    }

    private fun getMonthName(position: Int): String {
        return when(position) {
            0 -> getString(R.string.jan)
            1 -> getString(R.string.feb)
            2 -> getString(R.string.mar)
            3 -> getString(R.string.apr)
            4 -> getString(R.string.may)
            5 -> getString(R.string.jun)
            6 -> getString(R.string.jul)
            7 -> getString(R.string.aug)
            8 -> getString(R.string.sep)
            9 -> getString(R.string.oct)
            10 -> getString(R.string.nov)
            11 -> getString(R.string.dec)
            else -> ""
        }
    }

    private fun getDayOfToday(): Int = dailyDtRepo.getDayOfToday()
    private fun getFirstDayList(): List<Int> = dailyDtRepo.getFirstDayList()
    private fun getEndDayList(): List<Int> = dailyDtRepo.getEndDayList()
    private fun getMonthList(): List<Int> = dailyDtRepo.getMonthList()
    private fun getYearList(): List<Int> = dailyDtRepo.getYearList()

    private fun showToastMsg(msg: String) = Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}