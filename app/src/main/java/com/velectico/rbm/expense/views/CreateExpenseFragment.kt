package com.velectico.rbm.expense.views

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.navigation.Navigation

import com.velectico.rbm.R
import com.velectico.rbm.base.views.BaseFragmentWithMasterData
import com.velectico.rbm.databinding.FragmentCreateExpenseBinding
import com.velectico.rbm.expense.model.ExpenseCreateRequest
import com.velectico.rbm.expense.viewmodel.ExpenseViewModel
import com.velectico.rbm.masterdata.model.MasterDataItem
import com.velectico.rbm.masterdata.model.MasterDataResponse
import com.velectico.rbm.utils.*
import java.io.File
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class CreateExpenseFragment : BaseFragmentWithMasterData(), DatePickerDialog.OnDateSetListener {
    private lateinit var binding : FragmentCreateExpenseBinding
    private var imageUtils : ImageUtils?=null
    private var imageUrl : String? = null
    private var cameraImgUri : Uri?= null
    private var cuurentDatePicketParentView : com.google.android.material.textfield.TextInputEditText? = null
    private lateinit var expenseViewModel: ExpenseViewModel
    private var expenseList : MasterDataResponse = MasterDataResponse()
    private var selectedExpense : MasterDataItem?=null
    private var selectedBeat : MasterDataItem?=null
    //private  val btn_create_exp : AppCompatButton?=null;



    override fun getLayout(): Int {
        return R.layout.fragment_create_expense
    }

    override fun init(binding: ViewDataBinding) {
        this.binding = binding as FragmentCreateExpenseBinding
        imageUtils = ImageUtils(context as Context,baseActivity,this)
        observeViewModelData()
        setUp()
        getMasterDataListFromServer(EXPENSE_TYPE_MASTER_DATA,binding.progressLayout)
    }

    private fun setUp(){
        checkPermission()
        binding.btnCreateExp.setOnClickListener {
            createExpense();
        }
        binding.btnCaptureImg.setOnClickListener {
            cameraImgUri = imageUtils?.getImageUri()
            imageUtils?.captureImageIntent(cameraImgUri)
        }
        binding.btnSelectImg.setOnClickListener {
            imageUtils?.openAlbum()
        }
        binding.etDate.setOnClickListener {
            cuurentDatePicketParentView = this.binding.etDate
            showDatePickerDialog()
        }
        binding.etBeatName.setOnClickListener {
            binding.spBeatName.performClick()
        }
        binding.etExpenseType.setOnClickListener {
            binding.spExpense.performClick()
        }
    }

    private fun checkPermission(){
        val checkSelfPermission = ContextCompat.checkSelfPermission(baseActivity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(baseActivity, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }
    }

    override fun hideMasterDataLoader(masterDataType : String?) {
        if(EXPENSE_TYPE_MASTER_DATA == masterDataType){
            binding.progressLayout.visibility =View.GONE
        }
    }

    override fun masterDataReceived(masterDataType : String? , masterData : MasterDataResponse?) {
        if(EXPENSE_TYPE_MASTER_DATA == masterDataType){
            expenseList = masterData as MasterDataResponse
            initBeatSpinner()
            initExpenseTypeSpinner()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            imageUtils?.FINAL_TAKE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    imageUrl = imageUtils?.imgFilePath
                    imageUtils?.displayImage(imageUtils?.imgFilePath,binding.ivExpBill)
                }
            imageUtils?.FINAL_CHOOSE_PHOTO ->
                if (resultCode == Activity.RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        imageUrl = imageUtils?.handleImageOnKitkat(data)
                        imageUtils?.displayImage(imageUrl,binding.ivExpBill)
                    }
                }
        }
    }

    //callback function to set set once date is selected from datepicker
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val tempDate: Date = DateUtility.getDateFromYearMonthDay(year, month, dayOfMonth)
        val subDateString: String =
            DateUtility.getStringDateFromTimestamp((tempDate.time), DateUtility.YYYY_DASH_MM_DASH_DD)
        cuurentDatePicketParentView?.setText(subDateString)
    }

    //Function to show date picker dialog box for start and end date
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]
        DatePickerDialog(requireActivity(), this, year, month, dayOfMonth).show()
    }

    private fun initBeatSpinner() {
        //https://github.com/Chivorns/SmartMaterialSpinner
        val beatList: MutableList<String> = ArrayList()
        for (expenseItem in expenseList){
            beatList.add(expenseItem.Exp_Head_Name)
        }

        binding.spBeatName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                binding.etBeatName.setText( beatList[position])
                selectedBeat = expenseList?.get(position);
            }
            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
        binding.spBeatName.setItem(beatList)
    }

    private fun initExpenseTypeSpinner() {
        //https://github.com/Chivorns/SmartMaterialSpinner
        var expenseListStringArray: MutableList<String> = ArrayList()
        for (expenseItem in this.expenseList){
            expenseListStringArray.add(expenseItem.Exp_Head_Name)
        }
        binding.spExpense.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
                binding.etExpenseType.setText( expenseListStringArray[position])
                selectedExpense = expenseList.get(position)
            }
            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
        binding.spExpense.setItem(expenseListStringArray)
    }

    private fun observeViewModelData() {
        expenseViewModel = ExpenseViewModel.getInstance(baseActivity)
        expenseViewModel.expenseCreateResponse.removeObservers(viewLifecycleOwner);
        expenseViewModel.expenseCreateResponse.observe(viewLifecycleOwner, Observer { listResponse ->
            listResponse?.let {
                Log.e("test","i mherere1111--->"+listResponse.expenseId);
                Log.e("test","i mherere1111--->"+listResponse.respMessage);
                showAlertDialog(listResponse.respMessage)
               // adapter?.expenseList = expenseViewModel.expenseListResponse.value?.expenseDetails as List<Expense>
            }
        })

        expenseViewModel.loading.observe(this, Observer { progress ->
            binding?.progressLayout?.visibility = if(progress) View.VISIBLE else View.GONE
        })

        expenseViewModel.errorLiveData.observe(this, Observer {
            Log.e("test","i mherere33333");
           // (this as BaseActivity).showAlertDialog(it.errorMessage ?: getString(R.string.no_data_available))
        })
    }

    private fun showAlertDialog(msg:String?){
        val dialog = AlertDialog.Builder(context as Context)
        dialog.setTitle(getString(R.string.alert_title))
            .setMessage(msg)
            .setCancelable(false)
            .setNegativeButton(getString(R.string.ok_button)) {d, _ ->
                expenseViewModel.expenseCreateResponse.removeObservers(viewLifecycleOwner);
                d.dismiss()
                val nav  =  CreateExpenseFragmentDirections.actionCreateExpenseFragmentToExpenseListFragment()
                Navigation.findNavController(binding.btnSelectImg).navigate(nav);
            }
            .show()
    }

    private fun createExpense(){
        if(selectedExpense==null){
            showToastMessage("Please select expense type")
        }
        else if(selectedBeat==null){
            showToastMessage("Please select beat type")
        }
        else if(binding.etDate==null || binding.etDate.text.toString()?.trim() == ""){
            showToastMessage("Please select expense date")
        }
        else if(binding.etMiscAmt==null || binding.etMiscAmt.text.toString()?.trim() == ""){
            showToastMessage("Please provide a date")
        }
        else{

        }
    }



}
