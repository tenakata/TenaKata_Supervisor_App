package com.tenakatasupervisor.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.AndroidViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.tenakatasupervisor.Adapters.ModelBusinessDetails;
import com.tenakatasupervisor.Base.BaseFragment;
import com.tenakatasupervisor.Network.Authentication;
import com.tenakatasupervisor.R;
import com.tenakatasupervisor.Utilities.HRAppConstants;
import com.tenakatasupervisor.Utilities.HRPriceFormater;
import com.tenakatasupervisor.Utilities.HRUrlFactory;
import com.tenakatasupervisor.Utilities.HRValidationHelper;
import com.tenakatasupervisor.databinding.FragmentBusinessDetailsBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentBusinessDetails extends BaseFragment  {
    Context context;
    private PieData pieData,pieData2;
    private PieDataSet pieDataSet,pieDataSet2;
    private ArrayList pieEntries,pieEntriespurchase;
    FragmentBusinessDetailsBinding binding;
    private   RecyclerView.LayoutManager layoutManager;
    private ArrayList<ModelBusinessDetails> list=new ArrayList();
    private RecyclerView recyclerView;
    private String shopID;

    public FragmentBusinessDetails(String shopID){
        this.shopID = shopID;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()),R.layout.fragment_business_details, container, false);

       return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        try {
            hitApi();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getCircularProgressChart(String saleCash,String saleCredit,String purchaseCash,String purchaseCredit) {




        if ((saleCash.equals("0.0")||saleCash.equals("0")) && (saleCredit.equals("0.0")||saleCredit.equals("0"))){
            pieEntries = new ArrayList<>();
            pieEntries.add(new PieEntry(1f, 0));
            pieEntries.add(new PieEntry(1f, 1));
        }else {
            pieEntries = new ArrayList<>();
            pieEntries.add(new PieEntry(Float.parseFloat(saleCash), 0));
            pieEntries.add(new PieEntry(Float.parseFloat(saleCredit), 1));
        }


        pieDataSet = new PieDataSet(pieEntries, "");
        pieData = new PieData(pieDataSet);
        binding.pieChart.setData(pieData);
        binding.pieChart.animateXY(1000, 1000);
        binding.pieChart.setTouchEnabled(false);
        binding.pieChart.setEntryLabelColor(Color.BLUE);
        binding.pieChart.getLegend().setEnabled(false);
        pieData.setDrawValues(false);
        binding.pieChart.getDescription().setEnabled(false);
        //binding.pieChart.setTransparentCircleRadius(100f);
        binding.pieChart.setHoleRadius(80f);
        //pieDataSet.setSelectionShift(20);

        if (Double.parseDouble(saleCash)>0 && Double.parseDouble(saleCredit)>0){
            pieDataSet.setColors(getResources().getColor(R.color.colorGreen), getResources().getColor(R.color.coloOrange));
        }else if (Double.parseDouble(saleCash)==0 && Double.parseDouble(saleCredit)>0){
            pieDataSet.setColors(getResources().getColor(R.color.colorGray), getResources().getColor(R.color.coloOrange));
        }else if (Double.parseDouble(saleCash)>0 && Double.parseDouble(saleCredit)==0){
            pieDataSet.setColors(getResources().getColor(R.color.colorGreen), getResources().getColor(R.color.colorGray));
        }else {
            pieDataSet.setColors(getResources().getColor(R.color.colorGray), getResources().getColor(R.color.colorGray));
        }


        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(8f);
        pieDataSet.setSliceSpace(1f);


        /*======================Purchase=========================*/



        if ((purchaseCash.equals("0.0")||purchaseCash.equals("0")) && (purchaseCredit.equals("0.0")||purchaseCredit.equals("0"))){
            pieEntriespurchase = new ArrayList<>();
            pieEntriespurchase.add(new PieEntry(1f, 0));
            pieEntriespurchase.add(new PieEntry(1f, 1));
        }else {
            pieEntriespurchase = new ArrayList<>();
            pieEntriespurchase.add(new PieEntry(Float.parseFloat(purchaseCash), 0));
            pieEntriespurchase.add(new PieEntry(Float.parseFloat(purchaseCredit), 1));
        }

        pieDataSet2 = new PieDataSet(pieEntriespurchase, "");
        pieData2 = new PieData(pieDataSet2);
        binding.pieChart2.setData(pieData2);
        binding.pieChart2.animateXY(1000, 1000);
        binding.pieChart2.setTouchEnabled(false);
        binding.pieChart2.setEntryLabelColor(Color.BLUE);
        binding.pieChart2.getLegend().setEnabled(false);
        pieData2.setDrawValues(false);
        binding.pieChart2.getDescription().setEnabled(false);
        //binding.pieChart.setTransparentCircleRadius(100f);
        binding.pieChart2.setHoleRadius(80f);

        if (Double.parseDouble(purchaseCash)>0 && Double.parseDouble(purchaseCredit)>0){
            pieDataSet2.setColors(getResources().getColor(R.color.colorGreen), getResources().getColor(R.color.coloOrange));
        }else if (Double.parseDouble(purchaseCash)==0 && Double.parseDouble(purchaseCredit)>0){
            pieDataSet2.setColors(getResources().getColor(R.color.colorGray), getResources().getColor(R.color.coloOrange));
        }else if (Double.parseDouble(purchaseCash)>0 && Double.parseDouble(purchaseCredit)==0){
            pieDataSet2.setColors(getResources().getColor(R.color.colorGreen), getResources().getColor(R.color.colorGray));
        }else {
            pieDataSet2.setColors(getResources().getColor(R.color.colorGray), getResources().getColor(R.color.colorGray));
        }
        //pieDataSet.setSelectionShift(20);

        pieDataSet2.setValueTextColor(Color.WHITE);
        pieDataSet2.setValueTextSize(8f);
        pieDataSet2.setSliceSpace(1f);


    }



    private void hitApi() throws JSONException {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("shop_id",shopID);
        Authentication.object(getActivity(), HRUrlFactory.generateUrlWithVersion(HRAppConstants.URL_BUSINESSDETAIL),this,jsonObject);
    }



    @Override
    public void onTaskSuccess(Object responseObj) {
        if(responseObj instanceof ModelBusinessDetails){
            ModelBusinessDetails model=(ModelBusinessDetails) responseObj;
            binding.tvCashSales.setText("Cash Sale KES " +HRPriceFormater.roundDecimalByOneDigit(model.getResult().getCash_sale()));

            binding.tvCreditSale.setText("Credit Sale KES "+HRPriceFormater.roundDecimalByOneDigit(model.getResult().getCredit_sale()));
            binding.tvCashPurchase.setText("Cash Purchase KES "+HRPriceFormater.roundDecimalByOneDigit(model.getResult().getCash_purchase()));
            binding.tvCreditPurchase.setText(String.valueOf("Credit Purchase KES "+ HRPriceFormater.roundDecimalByOneDigit(model.getResult().getCredit_purchase())));
            binding.tvAverageSales.setText(HRPriceFormater.roundDecimalByOneDigit(model.getTotal_avrage_sales()));
            binding.tvAveragePurchase.setText(HRPriceFormater.roundDecimalByOneDigit(model.getTotal_avrage_purchase()));
            binding.total.setText("CASH KES "+ HRPriceFormater.roundDecimalByOneDigit(model.getTotal_cash()) +" & "+"CREDIT KES "+HRPriceFormater.roundDecimalByOneDigit(model.getTotal_credit()));

            getCircularProgressChart(HRValidationHelper.NullPrice(String.valueOf(model.getResult().getCash_sale())),
                    HRValidationHelper.NullPrice(String.valueOf(model.getResult().getCredit_sale())),
                    HRValidationHelper.NullPrice(String.valueOf(model.getResult().getCash_purchase())),
                    HRValidationHelper.NullPrice(String.valueOf(model.getResult().getCredit_purchase())));
        }
    }


    @Override
    public void onTaskError(String errorMsg) {
        super.onTaskError(errorMsg);
    }
}
