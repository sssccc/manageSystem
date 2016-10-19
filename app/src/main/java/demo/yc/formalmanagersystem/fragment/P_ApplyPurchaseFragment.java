package demo.yc.formalmanagersystem.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;

import cn.pedant.SweetAlert.SweetAlertDialog;
import demo.yc.formalmanagersystem.MainActivity;
import demo.yc.formalmanagersystem.MyApplication;
import demo.yc.formalmanagersystem.R;
import demo.yc.formalmanagersystem.UpdateListener;
import demo.yc.formalmanagersystem.models.Purchase;
import demo.yc.formalmanagersystem.util.VolleyUtil;

/**
 * Created by Administrator on 2016/7/31.
 */
public class P_ApplyPurchaseFragment extends Fragment implements View.OnClickListener {

    //提交成功后，所有信息重置
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            nameEdit.setText("");
            brandEdit.setText("");
            priceEdit.setText("");
            modelEdit.setText("");
            describeEdit.setText("");

            applyPurchase.setFocusable(true);
            applyPurchase.setFocusableInTouchMode(true);
            applyPurchase.requestFocus();
            applyPurchase.requestFocusFromTouch();

        }
    };


    //网络操作
    private VolleyUtil volleyUtil = new VolleyUtil();

    private Purchase purchase;
    private Button applyPurchase;
    private EditText nameEdit;
    private EditText brandEdit;
    private EditText priceEdit;
    private EditText modelEdit;
    private EditText describeEdit;

    private LinearLayout mContainer;

    private View topMenu;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.apply_purchase_layout_for_fragment, null);
        mContainer = (LinearLayout) view.findViewById(R.id.container);
        applyPurchase = (Button) view.findViewById(R.id.confirm_to_apply_purchase);
        nameEdit = (EditText) view.findViewById(R.id.name_edit_in_apply_purchase_page);
        brandEdit = (EditText) view.findViewById(R.id.brand_edit_in_apply_purchase_page);
        priceEdit = (EditText) view.findViewById(R.id.price_edit_in_apply_purchase_page);
        modelEdit = (EditText) view.findViewById(R.id.model_edit_in_apply_purchase_page);
        describeEdit = (EditText) view.findViewById(R.id.detail_of_repair_in_purchase_page);
        topMenu = getActivity().findViewById(R.id.top_layout_menu);
        topMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).showMenu();
            }
        });
        applyPurchase.setOnClickListener(this);
        purchase = new Purchase();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        float currentTranslationX = mContainer.getTranslationX();
        ObjectAnimator animator = ObjectAnimator.ofFloat(mContainer,"translationX",-1000f,currentTranslationX);
        animator.setDuration(3000);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(mContainer,"alpha",0f,1f);
        AnimatorSet set = new AnimatorSet();
        set.play(animator).with(animator1);
        set.setDuration(600);
        set.start();
        P_PropertyManagementFragment.isInitial = true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_to_apply_purchase:
                final String name = nameEdit.getText().toString();
                final String brand = brandEdit.getText().toString();
                final String price = priceEdit.getText().toString();
                final String model = modelEdit.getText().toString();
                final String describe = describeEdit.getText().toString();
                //信息未填写完整
                if ((TextUtils.isEmpty(name) || TextUtils.isEmpty(brand) ||
                        TextUtils.isEmpty(price) || TextUtils.isEmpty(model) ||
                        TextUtils.isEmpty(describe))) {
                    {
                        final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                        dialog.setTitleText("请输入完整信息！");
                        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                dialog.dismissWithAnimation();
                            }
                        });
                        dialog.show();
                        break;
                    }
                } else {
                    final SweetAlertDialog alarmDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                    alarmDialog.setTitleText("确认申请？");
                    alarmDialog.setCancelText("取消");
                    alarmDialog.setCancelable(false);
                    alarmDialog.setCanceledOnTouchOutside(false);
                    alarmDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    });
                    alarmDialog.setConfirmText("确认");
                    alarmDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(final SweetAlertDialog alarmDialog) {
                            alarmDialog.dismissWithAnimation();
                            final SweetAlertDialog progressDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.setCancelable(false);
                            progressDialog.setTitleText("");
                            progressDialog.setContentText("提交申请中，请稍等...");
                            progressDialog.show();

                            purchase.setName(name);
                            purchase.setBrand(brand);
                            purchase.setPrice(price);
                            purchase.setModel(model);
                            purchase.setDescribe(describe);
                            purchase.setCreaterIdentifier(MyApplication.getUser().getUsername());

                            volleyUtil.updatePurchaseInMySql(purchase, new UpdateListener() {
                                @Override
                                public void onSucceed(String s) {
                                    progressDialog.dismiss();
                                    SweetAlertDialog successDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                                    successDialog.setTitleText("提交成功!");
                                    successDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    });
                                    successDialog.show();
                                    handler.sendMessage(handler.obtainMessage());
                                }

                                @Override
                                public void onError(VolleyError error) {
                                    progressDialog.dismiss();
                                    SweetAlertDialog errorDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
                                    errorDialog.setTitleText("提交失败，请重试！");
                                    errorDialog.setConfirmText("确认");
                                    errorDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    });
                                    errorDialog.show();
                                }
                            });
                        }
                    });
                    alarmDialog.show();
                }
                break;
        }
    }
}
