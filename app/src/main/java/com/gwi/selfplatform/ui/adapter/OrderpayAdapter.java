package com.gwi.selfplatform.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.utils.BankUtil;
import com.gwi.selfplatform.common.utils.RefoundUtil;
import com.gwi.selfplatform.common.utils.StaticValueUtils;
import com.gwi.selfplatform.module.net.response.OrderQueryResults;

import java.util.List;
import java.util.Map;

public class OrderpayAdapter extends BaseAdapter {

    private List<String> mNeedRefundBussiness;

    private List<OrderQueryResults.OrderQueryResult> list;
    private LayoutInflater inflater;
    private Context mContext;
    private boolean loader = true;

    public OrderpayAdapter(List<OrderQueryResults.OrderQueryResult> list, Context mContext) {
        super();
        this.list = list;
        this.mContext = mContext;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Map<String, String> params = GlobalSettings.INSTANCE.getHospitalParams();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final OrderQueryResults.OrderQueryResult result = list.get(position);

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_order_pay, null);
            holder = new ViewHolder();
            holder.status_title = convertView.findViewById(R.id.status_title);
            holder.layout_notice = convertView.findViewById(R.id.layout_notice);
            holder.title_order = (TextView) convertView.findViewById(R.id.title_order);
            holder.busType = (TextView) convertView.findViewById(R.id.busType);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            holder.definy_name = (TextView) convertView.findViewById(R.id.definy_name);
            holder.cardNum = (TextView) convertView.findViewById(R.id.cardNum);
            holder.createTime = (TextView) convertView.findViewById(R.id.createTime);
            holder.payMoney = (TextView) convertView.findViewById(R.id.payMoney);
            holder.noticeTV = (TextView) convertView.findViewById(R.id.noticeTV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (loader) {
            loader = false;
            StaticValueUtils.clearOrderNoSataus();
            if (list != null && list.size() > 0) {
                System.out.println("order size:" + list.size());
                for (OrderQueryResults.OrderQueryResult item : list) {
                    String hStatus = item.getHisStatus();
                    String pStatus = item.getPayStatus();
                    String rStatus = item.getRefundStatus();
                    String businessStatus = item.getBusinessStatus();
                    String businessType = item.getBusinessType();
                    String orderStatus = RefoundUtil.getOrderStatus(businessType, pStatus, hStatus, businessStatus, rStatus);
                    if ("111".equals(orderStatus) &&
                            !StaticValueUtils.orderNoSataus.containsKey(item)) {
                        StaticValueUtils.putOrderNoSataus(item, true);
                        System.out.println("business succuss :" + item.getOrderNo());
                    }
                }
                System.out.println("OrderNoSataus size:" + StaticValueUtils.orderNoSataus.size());
            }
        }

        if (result != null) {
            holder.title_order.setText("订单号：" + result.getOrderNo());
            String businessType = result.getBusinessType();

            if (businessType.endsWith(BankUtil.BusinessType_Rgt)) {
                holder.busType.setText("挂号");
            } else if (businessType.endsWith(BankUtil.BusinessType_CARD_CHARGE)) {
                holder.busType.setText("预交金充值");
            } else if (businessType.endsWith(BankUtil.BusinessType_Recipe)) {
                holder.busType.setText("缴费");
            } else if (businessType.endsWith(BankUtil.BusinessType_Refound)) {
                holder.busType.setText("退预交金");
            } else if (businessType.endsWith(BankUtil.BusinessType_Rgt_Order)) {
                holder.busType.setText("预约挂号");
            }

            String pStatus = result.getPayStatus();
            String hStatus = result.getHisStatus();
            String rStatus = result.getRefundStatus();
            String bStatus = result.getBusinessStatus();
            String orderStatus = RefoundUtil.getOrderStatus(businessType, pStatus, hStatus, bStatus, rStatus);
            String orderResult = null;

//            if (StaticValueUtils.orderNoSataus.containsKey(result)) {
            if(businessType.equals(BankUtil.BusinessType_CARD_CHARGE)&&orderStatus=="11"/*充值成功*/) {
                holder.status.setTextColor(mContext.getResources().getColor(R.color.text_green));
            } else {
                holder.status.setTextColor(mContext.getResources().getColor(R.color.red_hos_personal));
            }


            /**refoundType:
             *  挂号 = 1,
             现金充值 = 2,
             银行充值 = 3,
             缴费 = 4,
             退预交金 = 5,
             支付宝预交金充值 = 6,
             预约挂号 = 7
             */
            int resId = 0;
            if (businessType.equalsIgnoreCase(BankUtil.BusinessType_Rgt_Order)) {//预约挂号
                resId = getAppointHint2(orderStatus);
            } else if (BankUtil.BusinessType_Rgt.equals(businessType)) {
                resId = getRegistrationHint(orderStatus);
            } else if (BankUtil.BusinessType_Recipe.equals(businessType)) {
                resId = getPaymentHint(orderStatus);
            } else if (BankUtil.BusinessType_CARD_CHARGE.equals(businessType)) {
                resId = getReChargeHint(orderStatus);
            }else if (BankUtil.BusinessType_Refound.equals(businessType)) {
                resId = getRefundOrderHint(orderStatus);
            }
            if (resId != 0) {
                orderResult = mContext.getString(resId);
                holder.status.setText(orderResult);
            }

            if (orderResult == null) {
                holder.status_title.setVisibility(View.GONE);
            }

            holder.definy_name.setText(result.getUserName());
            holder.cardNum.setText(result.getCardNo());
            holder.createTime.setText(result.getCreateTime());
            holder.payMoney.setText(result.getAmount() + "元");
        }

        return convertView;
    }

    private int getRegistrationHint(String orderStatus) {
        if ("10xx".equalsIgnoreCase(orderStatus)) {
            return R.string.phor_regist_10xx;
        } else if ("110x".equalsIgnoreCase(orderStatus)) {
            return R.string.phor_regist_110x;
        } else if ("111x".equalsIgnoreCase(orderStatus)) {
            return R.string.phor_regist_111x;
        } else if ("112x".equalsIgnoreCase(orderStatus)) {
            return R.string.phor_regist_112x;
        } else if ("113x".equalsIgnoreCase(orderStatus)) {
            return R.string.phor_regist_113x;
        } else if ("12xx".equalsIgnoreCase(orderStatus)
                || "13xx".equalsIgnoreCase(orderStatus)) {
            return R.string.phor_regist_1xxx;
        } else if ("0x".equals(orderStatus)) {
            return R.string.nonPay;
        }
        return 0;
    }

    private int getAppointHint(String orderStatus) {
        if ("10xx".equalsIgnoreCase(orderStatus)) {
            return R.string.phor_appoint_10xx;
        } else if ("110x".equalsIgnoreCase(orderStatus)) {
            return R.string.phor_appoint_110x;
        } else if ("111x".equalsIgnoreCase(orderStatus)) {
            return R.string.phor_appoint_111x;
        } else if ("112x".equalsIgnoreCase(orderStatus)) {
            return R.string.phor_appoint_112x;
        } else if ("113x".equalsIgnoreCase(orderStatus)) {
            return R.string.phor_appoint_113x;
        } else if ("1220".equalsIgnoreCase(orderStatus)) {
            return R.string.phor_appoint_1200;
        } else if ("1224".equalsIgnoreCase(orderStatus)) {
            return R.string.phor_appoint_1201;
        } else if ("1222".equalsIgnoreCase(orderStatus)
                || "1223".equalsIgnoreCase(orderStatus)) {
            return R.string.phor_appoint_120x;
        } else if ("13xx".equalsIgnoreCase(orderStatus)) {
            return R.string.phor_appoint_13xx;
        } else if ("0x".equals(orderStatus)) {
            return R.string.nonPay;
        }
        return 0;
    }

    private int getAppointHint2(String orderStatus) {
        if ("100x".equalsIgnoreCase(orderStatus)) {
            return R.string.phor_appoint_100x;
        } else if ("111x".equalsIgnoreCase(orderStatus)) {
            return R.string.phor_appoint_111x;
        } else if ("1223".equalsIgnoreCase(orderStatus)) {
            return R.string.phor_appoint_1220;
        } else if ("1221".equalsIgnoreCase(orderStatus)) {
            return R.string.phor_appoint_1221;
        } else if ("1225".equalsIgnoreCase(orderStatus)||"1222".equalsIgnoreCase(orderStatus)) {
            return R.string.phor_appoint_1222;
        }else if ("1224".equalsIgnoreCase(orderStatus)) {
            return R.string.phor_appoint_1224;
        } else if ("133x".equalsIgnoreCase(orderStatus)) {
            return R.string.phor_appoint_133x;
        } else if ("0x".equals(orderStatus)) {
            return R.string.nonPay;
        }
        return 0;
    }

    /**
     * 缴费
     *
     * @param orderStatus
     * @return
     */
    private int getPaymentHint(String orderStatus) {
        if ("10xx".equalsIgnoreCase(orderStatus)) {
            return R.string.phor_payment_10xx;
        } else if ("110x".equalsIgnoreCase(orderStatus)) {
            return R.string.phor_payment_110x;
        } else if ("111x".equalsIgnoreCase(orderStatus)) {
            return R.string.phor_payment_111x;
        } else if ("112x".equalsIgnoreCase(orderStatus)) {
            return R.string.phor_payment_112x;
        } else if ("113x".equalsIgnoreCase(orderStatus)) {
            return R.string.phor_payment_113x;
        } else if ("1200".equalsIgnoreCase(orderStatus)) {
            return R.string.phor_payment_113x;
        } else if ("12xx".equalsIgnoreCase(orderStatus)
                || "13xx".equalsIgnoreCase(orderStatus)) {
            return R.string.phor_payment_1xxx;
        } else if ("0x".equals(orderStatus)) {
            return R.string.nonPay;
        }
        return 0;
    }

    /**
     * 充值
     *
     * @param orderStatus
     * @return
     */
    private int getReChargeHint(String orderStatus) {
        if ("10".equalsIgnoreCase(orderStatus)) {
            return R.string.phor_recharge_10;
        } else if ("11".equalsIgnoreCase(orderStatus)) {
            return R.string.phor_recharge_11;
        } else if ("12".equalsIgnoreCase(orderStatus)
                || "13".equalsIgnoreCase(orderStatus)) {
            return R.string.phor_recharge_1x;
        } else if ("0x".equals(orderStatus)) {
            return R.string.nonPay;
        }
        return 0;
    }

    private int getRefundOrderHint(String orderStatus) {
        if (orderStatus.equals("1111")) {
            return R.string.phor_refound_1;
        }else if (orderStatus.equals("1114")) {
            return R.string.phor_refound_4;
        }else if (orderStatus.equals("1115")) {
            return R.string.phor_refound_5;
        }
        return 0;
    }

    class ViewHolder {
        View status_title;
        View layout_notice;

        TextView title_order;
        TextView busType;
        TextView status;
        TextView definy_name;
        TextView cardNum;
        TextView createTime;
        TextView payMoney;
        TextView noticeTV;
    }


}
