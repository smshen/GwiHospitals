package com.gwi.selfplatform.ui.activity.query;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.response.G1035;
import com.gwi.selfplatform.module.net.response.G1036;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016-4-12.
 */
public class HospInfoDetailActivity extends HospBaseActivity {
    private final static String TAG = HospInfoDetailActivity.class.getSimpleName();

    private G1035 mItem;
    private String mHtmlContent = "<p style=\"line-height: 22pt\" class=\"MsoNormal\" align=\"left\"><span style=\"color: rgb(0,0,0)\"><span style=\"font-family: 宋体\" lang=\"EN-US\">&nbsp; &nbsp;&nbsp;</span><span style=\"font-family: 宋体\">湘西自治州人民医院（吉首大学第一附属医院）始建于<span lang=\"EN-US\">1952</span>年，是湘西自治州唯一的集医疗、教学、科研、预防、保健、康复为一体的国家三级综合性医院，是全国<span lang=\"EN-US\">500</span>家大型综合医院会员医院，卫生部批准的国际救援中心网络医院、国家级流感监测哨点医院、卫生部和湖南省医院感染监测网络会员单位、高等医学院校临床教学基地、省卫生厅核定的全科医师培训基地、湘西州继续医学教育培训基地、吉首大学临床学院、湘雅二医院协作医院和香港<span lang=\"EN-US\">&ldquo;</span>关怀行动<span lang=\"EN-US\">&rdquo;</span>湖南工作站。</span></span><span style=\"font-family: 宋体; color: dimgray; mso-bidi-font-size: 10.5pt; mso-bidi-font-family: 宋体; mso-font-kerning: 0pt\"><span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
            "<p style=\"line-height: 22pt\" class=\"MsoNormal\" align=\"left\"><span style=\"color: rgb(0,0,0)\"><span style=\"font-family: 宋体\" lang=\"EN-US\">&nbsp;&nbsp; &nbsp;</span><span style=\"font-family: 宋体\">医院占地<span lang=\"EN-US\">530</span>亩，建筑总面积<span lang=\"EN-US\">18</span>万平方米，设有党政管理科室20个，临床医技科室<span lang=\"EN-US\">58</span>个，资产总额<span lang=\"EN-US\">11.36</span>亿元。定编床位<span lang=\"EN-US\">1200</span>张，可开放床位<span lang=\"EN-US\">1700</span>张，医疗服务覆盖湘、鄂、渝、黔四省、市边区<span lang=\"EN-US\">20</span>多个县市。在职职工<span lang=\"EN-US\">1791</span>人，其中博士、硕士研究生<span lang=\"EN-US\">101</span>人，高级职称专业技术人员<span lang=\"EN-US\">253</span>人，中级职称<span lang=\"EN-US\">438</span>人，享受国务院特殊津贴专家<span lang=\"EN-US\">2</span>人，获国际南丁格尔奖<span lang=\"EN-US\">1</span>人。</span></span><span style=\"font-family: 宋体; color: dimgray; mso-bidi-font-size: 10.5pt; mso-bidi-font-family: 宋体; mso-font-kerning: 0pt\"><span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
            "<p style=\"line-height: 22pt; text-indent: 21pt\" class=\"MsoNormal\" align=\"left\"><span style=\"color: rgb(0,0,0)\"><span style=\"font-family: 宋体\">医院拥有飞利浦大平板血管造影<span lang=\"EN-US\">X</span>射线系统、美国<span lang=\"EN-US\">GE</span>公司<span lang=\"EN-US\">1250mA</span>大<span lang=\"EN-US\">C</span>臂、美国瓦里安直线加速器、适形调强治疗系统、德国西门子螺旋<span lang=\"EN-US\">CT</span>、德国西门子<span lang=\"EN-US\">ECT</span>、德国西门子<span lang=\"EN-US\">1.5T</span>磁共振成像系统、德国西门子<span lang=\"EN-US\">128</span>层螺旋<span lang=\"EN-US\">CT</span>、飞利浦<span lang=\"EN-US\">U</span>型臂<span lang=\"EN-US\">DR</span>、<span lang=\"EN-US\">7</span>门<span lang=\"EN-US\">3</span>舱<span lang=\"EN-US\">38</span>座大型高压氧舱群、<span lang=\"EN-US\">GE</span>公司全数字化<span lang=\"EN-US\">X</span>射线摄影系统、眼科准分子激光治疗仪、日本岛津动态平板数字胃肠机、日本<span lang=\"EN-US\">7600</span>全自动生化分析仪、四维彩超、日本东芝模拟定位机和西门子彩超等大型设备。<span lang=\"EN-US\">100</span>万元以上现代化医疗设备<span lang=\"EN-US\">38</span>台<span lang=\"EN-US\">(</span>件<span lang=\"EN-US\">)</span>，万元以上设备<span lang=\"EN-US\">1690</span>台<span lang=\"EN-US\">(</span>件<span lang=\"EN-US\">)</span>。</span></span><span style=\"font-family: 宋体; color: dimgray; mso-bidi-font-size: 10.5pt; mso-bidi-font-family: 宋体; mso-font-kerning: 0pt\"><span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
            "<p style=\"line-height: 22pt\" class=\"MsoNormal\" align=\"left\"><span style=\"color: rgb(0,0,0)\"><span style=\"font-family: 宋体\" lang=\"EN-US\">&nbsp;&nbsp; &nbsp;</span><span style=\"font-family: 宋体\">从八十年代开展体外循环心脏直视手术以来，先后成功地开展了高位食管癌切除术、复杂纵隔肿瘤切除术、支气管成形术、胰头癌根治术、断肢（腕）再植术、人工关节置换术、心血管疾病介入治疗、眼科准分子激光治疗近视、经皮椎间盘镜椎间盘切吸术、巨大听神经瘤切除术、脑干肿瘤切除术、经尿道前列腺气化切除术、人工晶体植入术、角膜移植术、肝癌肺癌动脉灌注加栓塞治疗术、射频消融术、冠脉造影<span lang=\"EN-US\">PT-CA</span>溶栓、临时和永久性心脏起博器安装术、无痛胃镜诊疗术、经十二指肠乳头切开取石术、各种放射治疗、非体外循环心脏不停跳冠脉搭桥术、胡桃夹综合征介入手术、耳再造术等多项高难度手术及治疗。</span></span><span style=\"font-family: 宋体; color: dimgray; mso-bidi-font-size: 10.5pt; mso-bidi-font-family: 宋体; mso-font-kerning: 0pt\"><span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
            "<p style=\"line-height: 22pt\" class=\"MsoNormal\" align=\"left\"><span style=\"color: rgb(0,0,0)\"><span style=\"font-family: 宋体\" lang=\"EN-US\">&nbsp;&nbsp; &nbsp;</span><span style=\"font-family: 宋体\">近年来，医院获得卫生部科技进步二等奖<span lang=\"EN-US\">1</span>项，国家专利<span lang=\"EN-US\">3</span>项，省科技进步奖<span lang=\"EN-US\">5</span>项，省青年科技奖<span lang=\"EN-US\">1</span>人，州科技进步奖<span lang=\"EN-US\">28</span>项，州青年科技奖<span lang=\"EN-US\">4</span>人。骨科、神经外科被湖南省卫生厅定为临床重点学科。</span></span><span style=\"font-family: 宋体; color: dimgray; mso-bidi-font-size: 10.5pt; mso-bidi-font-family: 宋体; mso-font-kerning: 0pt\"><span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
            "<p style=\"line-height: 22pt\" class=\"MsoNormal\" align=\"left\"><span style=\"color: rgb(0,0,0)\"><span style=\"font-family: 宋体\" lang=\"EN-US\">&nbsp;&nbsp; &nbsp;</span><span style=\"font-family: 宋体\">医院先后被授予全国精神文明先进单位、全国模范职工之家、全国民族团结先进单位、湖南省文明单位、湖南省职工职业道德建设十佳单位、湖南省文明先进集体和湘西州文明建设先进单位等荣誉称号。</span></span><span style=\"font-family: 宋体; color: dimgray; mso-bidi-font-size: 10.5pt; mso-bidi-font-family: 宋体; mso-font-kerning: 0pt\"><span lang=\"EN-US\"><o:p></o:p></span></span></p>\n" +
            "<p>&nbsp;</p>";

    @Bind(R.id.txt_title)
    TextView mTxtTitle;
    @Bind(R.id.txt_sub_title)
    TextView mTxtSubTitle;
    @Bind(R.id.txt_content)
    TextView mTxtContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosp_info_detail);
        ButterKnife.bind(this);

        mItem = (G1035) getIntent().getExtras().getSerializable(Constants.KEY_BUNDLE);

        String type = mItem.getTypeID();

        // 初始化标题
        if (Constants.HospInfoQuery.HOSPITAL_NEWS.equals(type)) {
            setTitle(R.string.label_hospital_news);
        } else if (Constants.HospInfoQuery.TREATMENT_GUIDELINES.equals(type)) {
            setTitle(R.string.label_treatment_guidelines);
        }

//        mTxtTitle.setText(mItem.getTitle());
//        mTxtSubTitle.setText(mItem.getCreateUserName());
//        mTxtContent.setText(Html.fromHtml(mHtmlContent));

        request("110");
    }

    private void request(String directoryId) {
        ApiCodeTemplate.queryHospDetail(this, TAG, directoryId, new RequestCallback<G1036>() {
            @Override
            public void onRequestSuccess(G1036 result) {
                if (null != result) {
                    mTxtTitle.setText(result.getTitle());
                    mTxtSubTitle.setText(result.getCreateUserName());
                    mTxtContent.setText(Html.fromHtml(result.getContent()));
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                mTxtTitle.setText(mItem.getTitle());
                mTxtSubTitle.setText(mItem.getCreateUserName());
                mTxtContent.setText(Html.fromHtml(mHtmlContent));
            }
        });
    }
}
