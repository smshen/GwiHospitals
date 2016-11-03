package com.gwi.selfplatform.module.net.response;

import java.util.List;

public class G2210 {
    private String PatientID;
    private String PatName;
    private String ChargeType;
    private String DepName;
    private String TotalFee;
    private String PrintDate;
    private String StartDate;
    private String EndDate;
    
    private List<GItem> mItems;
    
    public List<GItem> getItems() {
        return mItems;
    }

    public void setItems(List<GItem> items) {
        mItems = items;
    }

    public String getPatientID() {
        return PatientID;
    }



    public String getPatName() {
        return PatName;
    }



    public String getChargeType() {
        return ChargeType;
    }



    public String getDepName() {
        return DepName;
    }



    public String getTotalFee() {
        return TotalFee;
    }



    public String getPrintDate() {
        return PrintDate;
    }



    public String getStartDate() {
        return StartDate;
    }



    public String getEndDate() {
        return EndDate;
    }



    public void setPatientID(String patientID) {
        PatientID = patientID;
    }



    public void setPatName(String patName) {
        PatName = patName;
    }



    public void setChargeType(String chargeType) {
        ChargeType = chargeType;
    }



    public void setDepName(String depName) {
        DepName = depName;
    }



    public void setTotalFee(String totalFee) {
        TotalFee = totalFee;
    }



    public void setPrintDate(String printDate) {
        PrintDate = printDate;
    }



    public void setStartDate(String startDate) {
        StartDate = startDate;
    }



    public void setEndDate(String endDate) {
        EndDate = endDate;
    }



    public static class GItem {
        private String Date;
        private String ItemClass;
        private String ItemName;
        private String Standard;
        private String Units;
        private String Price;
        private String Number;
        private String ItemFee;
        private String ExecDept;
        private String Note;
        public String getDate() {
            return Date;
        }
        public String getItemClass() {
            return ItemClass;
        }
        public String getItemName() {
            return ItemName;
        }
        public String getStandard() {
            return Standard;
        }
        public String getUnits() {
            return Units;
        }
        public String getPrice() {
            return Price;
        }
        public String getNumber() {
            return Number;
        }
        public String getItemFee() {
            return ItemFee;
        }
        public String getExecDept() {
            return ExecDept;
        }
        public String getNote() {
            return Note;
        }
        public void setDate(String date) {
            Date = date;
        }
        public void setItemClass(String itemClass) {
            ItemClass = itemClass;
        }
        public void setItemName(String itemName) {
            ItemName = itemName;
        }
        public void setStandard(String standard) {
            Standard = standard;
        }
        public void setUnits(String units) {
            Units = units;
        }
        public void setPrice(String price) {
            Price = price;
        }
        public void setNumber(String number) {
            Number = number;
        }
        public void setItemFee(String itemFee) {
            ItemFee = itemFee;
        }
        public void setExecDept(String execDept) {
            ExecDept = execDept;
        }
        public void setNote(String note) {
            Note = note;
        }
    }
}
