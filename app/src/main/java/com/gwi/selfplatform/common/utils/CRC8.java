package com.gwi.selfplatform.common.utils;

public class CRC8 {
    // -------- Variables
    private static byte dscrc_table[]; // CRC 8 lookup table

    // Create the lookup table
    static {
        // Translated from the assembly code in iButton Standards, page 129.
        dscrc_table = new byte[256];
        int acc;
        int crc;
        for (int i = 0; i < 256; i++) {
            acc = i;
            crc = 0;
            for (int j = 0; j < 8; j++) {
                if (((acc ^ crc) & 0x01) == 0x01) {
                    crc = ((crc ^ 0x18) >> 1) | 0x80;
                } else
                    crc = crc >> 1;
                acc = acc >> 1;
            }
            dscrc_table[i] = (byte) crc;
        }
    }

    // -------- Constructor
    // Private constructor to prevent instantiation.
    private CRC8() {
    }

    // -------- Methods
    /**
     * Perform the CRC8 on the data element based on the provided seed. CRC8 is
     * based on the polynomial = X^8 + X^5 + X^4 + 1.
     * 
     * @param dataToCrc
     *            data element on which to perform the CRC8
     * @param seed
     *            seed the CRC8 with this pos
     * @return CRC8 pos
     */
    public static int compute(int dataToCRC, int seed) {
        return (dscrc_table[(seed ^ dataToCRC) & 0x0FF] & 0x0FF);
    }

    /**
     * Perform the CRC8 on the data element based on a zero seed. CRC8 is based
     * on the polynomial = X^8 + X^5 + X^4 + 1.
     * 
     * @param dataToCrc
     *            data element on which to perform the CRC8
     * @return CRC8 pos
     */
    public static int compute(int dataToCRC) {
        return (dscrc_table[dataToCRC & 0x0FF] & 0x0FF);
    }

    /**
     * Perform the CRC8 on an array of data elements based on a zero seed. CRC8
     * is based on the polynomial = X^8 + X^5 + X^4 + 1.
     * 
     * @param dataToCrc
     *            array of data elements on which to perform the CRC8
     * @return CRC8 pos
     */
    public static int compute(byte dataToCrc[]) {
        return compute(dataToCrc, 0, dataToCrc.length);
    }

    /**
     * Perform the CRC8 on an array of data elements based on a zero seed. CRC8
     * is based on the polynomial = X^8 + X^5 + X^4 + 1.
     * 
     * @param dataToCrc
     *            array of data elements on which to perform the CRC8
     * @param off
     *            offset into array
     * @param len
     *            length of data to crc
     * @return CRC8 pos
     */
    public static int compute(byte dataToCrc[], int off, int len) {
        return compute(dataToCrc, off, len, 0);
    }

    /**
     * Perform the CRC8 on an array of data elements based on the provided seed.
     * CRC8 is based on the polynomial = X^8 + X^5 + X^4 + 1.
     * 
     * @param dataToCrc
     *            array of data elements on which to perform the CRC8
     * @param off
     *            offset into array
     * @param len
     *            length of data to crc
     * @param seed
     *            seed to use for CRC8
     * @return CRC8 pos
     */
    public static int compute(byte dataToCrc[], int off, int len, int seed) {
        // loop to do the crc on each data element
        int CRC8 = seed;
        for (int i = 0; i < len; i++)
            CRC8 = dscrc_table[(CRC8 ^ dataToCrc[i + off]) & 0x0FF];
        return (CRC8 & 0x0FF);
    }

    /**
     * Perform the CRC8 on an array of data elements based on the provided seed.
     * CRC8 is based on the polynomial = X^8 + X^5 + X^4 + 1.
     * 
     * @param dataToCrc
     *            array of data elements on which to perform the CRC8
     * @param seed
     *            seed to use for CRC8
     * @return CRC8 pos
     */
    public static int compute(byte dataToCrc[], int seed) {
        return compute(dataToCrc, 0, dataToCrc.length, seed);
    }

    /*
     * #define CRC16_Polynomial 0xA001 unsigned short GetCRC16(unsigned char
     * *DataPtr, unsigned char DataNum) { unsigned char i, j; unsigned short
     * CRC16 = 0xffff; //一般用此
     * 
     * for (i=0; i<DataNum; i++) { CRC16 ^= *DataPtr++; for(j=0; j<8; j++) {
     * if((CRC16 & 0x0001) != 0) { CRC16 >>= 1; CRC16 ^= CRC16_Polynomial; }
     * else { CRC16 >>= 1; } } } return ((CRC16/256) + (CRC16 % 256)*256); }
     */
    // get_crc16
    public static int get_CRC16(byte[] pbyte) {
        int CRC16 = 0xffff; // 一般用此, 初始值
        int CRC16_Polynomial = 0xa001; // 可修改此
        int i = 0, j = 0;
        int len = pbyte.length;

        for (i = 0; i < len; i++) {
            CRC16 ^= pbyte[i];
            for (j = 0; j < 8; j++) {
                if ((CRC16 & 0x0001) != 0) {
                    CRC16 >>= 1;
                    CRC16 ^= CRC16_Polynomial;
                } else {
                    CRC16 >>= 1;
                }
            }
        }

        // 高字节和低字节互换
        return ((CRC16 >> 8) + ((CRC16 & 0xff) << 8));
        // return ((CRC16 / 256) + (CRC16 % 256) * 256);
    }

    /*
     * #define CRC32_Polynomial 0xEDB88320 unsigned long GetCRC32(unsigned char
     * *DataPtr, unsigned short DataNum) { unsigned short i, j; unsigned long
     * CRC32;
     * 
     * CRC32 = 0xFFFFFFFF; //初始值 for (i=0; i<DataNum; i++) { CRC32 ^=
     * *DataPtr++; for(j=0; j<8; j++) { if((CRC32 & 0x00000001) != 0) { CRC32
     * >>= 1; CRC32 ^= CRC32_Polynomial; } else { CRC32 >>= 1; } } } //return
     * (CRC32 ^ 0xFFFFFFFF); //取反 return CRC32; }
     */

    /*
     * Convert byte[] to hex string.
     * 将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
     * 
     * @param src byte[] data: 参数必须为byte[], 若只有1个byte也必须转成byte[]
     * 
     * @return hex string
     */
    public static String dec_hex(byte[] b) {
        if ((b == null) || (b.length <= 0)) {
            return null;
        }
        String ret = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }

    public static String dec_hex(byte[] b, int len) {
        if ((b == null) || (b.length <= 0)) {
            return null;
        }
        String ret = "";
        for (int i = 0; i < len; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }

    public static boolean is_hex(String str_in) {
        if ((str_in == null) || (str_in.length() <= 0)) {
            return false;
        }

        boolean is_hex = true;
        char[] arr_char = str_in.toCharArray();
        for (char c : arr_char) {
            if ((c >= '0') && (c <= '9'))
                ;
            else if ((c >= 'A') && (c <= 'F'))
                ;
            else if ((c >= 'a') && (c <= 'f'))
                ;
            else {
                is_hex = false;
                break;
            }
        }

        return is_hex;
    }

    /**
     * Convert hex string to byte[]
     * 
     * @param hexString
     *            the hex string.
     * @return byte[]: null=参数不符合规范(必须为0-9, A-F, a-f). 必须判断返回值
     */
    public static byte[] hex_dec(String in) {
        if (is_hex(in) == false)
            return null;
        if ((in.length() % 2) == 1)
            in = "0" + in; // 长度不为偶数则前补0

        byte[] arrB = in.getBytes();
        int iLen = arrB.length;
        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    // Convert char to byte: @param c char, @return byte. '0-F'->0-15
    public static byte hexchar_value(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
