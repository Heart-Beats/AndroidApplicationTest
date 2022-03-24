package com.hl.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by chibb on 2019/9/11.
 */
object IdCardUtil {

    /**
     * <pre>
     * 省、直辖市代码表：
     * 11 : 北京  12 : 天津  13 : 河北       14 : 山西  15 : 内蒙古
     * 21 : 辽宁  22 : 吉林  23 : 黑龙江  31 : 上海  32 : 江苏
     * 33 : 浙江  34 : 安徽  35 : 福建       36 : 江西  37 : 山东
     * 41 : 河南  42 : 湖北  43 : 湖南       44 : 广东  45 : 广西      46 : 海南
     * 50 : 重庆  51 : 四川  52 : 贵州       53 : 云南  54 : 西藏
     * 61 : 陕西  62 : 甘肃  63 : 青海       64 : 宁夏  65 : 新疆
     * 71 : 台湾
     * 81 : 香港  82 : 澳门
     * 91 : 国外
    </pre> *
     */
    private val cityCode = arrayOf(
        "11",
        "12",
        "13",
        "14",
        "15",
        "21",
        "22",
        "23",
        "31",
        "32",
        "33",
        "34",
        "35",
        "36",
        "37",
        "41",
        "42",
        "43",
        "44",
        "45",
        "46",
        "50",
        "51",
        "52",
        "53",
        "54",
        "61",
        "62",
        "63",
        "64",
        "65",
        "71",
        "81",
        "82",
        "91"
    )

    /**
     * 每位加权因子
     */
    private val power = intArrayOf(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2)

    /**
     * 验证所有的身份证的合法性
     *
     * @param idcard
     * 身份证
     * @return 合法返回true，否则返回false
     */
    fun isValidatedAllIdcard(idcard: String?): Boolean {
        if (idcard == null || "" == idcard) {
            return false
        }
        val s = 15
        if (idcard.length == s) {
            return validate15IDCard(idcard)
        }
        val s1 = 18
        return if (idcard.length == s1) {
            validate18Idcard(idcard)
        } else false

    }

    /**
     *
     *
     * 判断18位身份证的合法性
     *
     * 根据〖中华人民共和国国家标准GB11643-1999〗中有关公民身份号码的规定，公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成。
     * 排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。
     *
     *
     * 顺序码: 表示在同一地址码所标识的区域范围内，对同年、同月、同 日出生的人编定的顺序号，顺序码的奇数分配给男性，偶数分配 给女性。
     *
     *
     *
     * 1.前1、2位数字表示：所在省份的代码； 2.第3、4位数字表示：所在城市的代码； 3.第5、6位数字表示：所在区县的代码；
     * 4.第7~14位数字表示：出生年、月、日； 5.第15、16位数字表示：所在地的派出所的代码；
     * 6.第17位数字表示性别：奇数表示男性，偶数表示女性；
     * 7.第18位数字是校检码：也有的说是个人信息码，一般是随计算机的随机产生，用来检验身份证的正确性。校检码可以是0~9的数字，有时也用x表示。
     *
     *
     *
     * 第十八位数字(校验码)的计算方法为： 1.将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7 9 10 5 8 4
     * 2 1 6 3 7 9 10 5 8 4 2
     *
     *
     *
     * 2.将这17位数字和系数相乘的结果相加。
     *
     *
     *
     * 3.用加出来和除以11，看余数是多少
     *
     * 4.余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5 4 3
     * 2。
     *
     *
     * 5.通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。如果余数是10，身份证的最后一位号码就是2。
     *
     *
     * @param idcard
     * @return
     */
    fun validate18Idcard(idcard: String?): Boolean {
        if (idcard == null) {
            return false
        }

        // 非18位为假
        val s = 18
        if (idcard.length != s) {

            return false
        }
        // 获取前17位
        val idcard17 = idcard.substring(0, 17)

        // 前17位全部为数字
        if (!isDigital(idcard17)) {
            return false
        }

        val provinceid = idcard.substring(0, 2)
        // 校验省份
        if (!checkProvinceid(provinceid)) {
            return false
        }

        // 校验出生日期
        val birthday = idcard.substring(6, 14)

        val sdf = SimpleDateFormat("yyyyMMdd")

        try {
            val birthDate = sdf.parse(birthday)
            val tmpDate = sdf.format(birthDate)
            // 出生年月日不正确
            if (tmpDate != birthday) {
                return false
            }

        } catch (e1: ParseException) {

            return false
        }

        // 获取第18位
        val idcard18Code = idcard.substring(17, 18)

        val c = idcard17.toCharArray()

        val bit = converCharToInt(c)

        var sum17: Int

        sum17 = getPowerSum(bit)

        // 将和值与11取模得到余数进行校验码判断
        val checkCode = getCheckCodeBySum(sum17) ?: return false
// 将身份证的第18位与算出来的校码进行匹配，不相等就为假
        return idcard18Code.equals(checkCode, ignoreCase = true)
        //System.out.println("正确");
    }

    /**
     * 校验15位身份证
     *
     * <pre>
     * 只校验省份和出生年月日
    </pre> *
     *
     * @param idcard
     * @return
     */
    fun validate15IDCard(idcard: String?): Boolean {
        if (idcard == null) {
            return false
        }
        // 非15位为假
        val s = 15
        if (idcard.length != s) {
            return false
        }

        // 15全部为数字
        if (!isDigital(idcard)) {
            return false
        }

        val provinceid = idcard.substring(0, 2)
        // 校验省份
        if (!checkProvinceid(provinceid)) {
            return false
        }

        val birthday = idcard.substring(6, 12)

        val sdf = SimpleDateFormat("yyMMdd")

        try {
            val birthDate = sdf.parse(birthday)
            val tmpDate = sdf.format(birthDate)
            // 身份证日期错误
            if (tmpDate != birthday) {
                return false
            }

        } catch (e1: ParseException) {

            return false
        }

        return true
    }

    /**
     * 将15位的身份证转成18位身份证
     *
     * @param idcard
     * @return
     */
    fun convertIdcarBy15bit(idcard: String?): String? {
        if (idcard == null) {
            return null
        }

        // 非15位身份证
        val s = 15
        if (idcard.length != s) {
            return null
        }

        // 15全部为数字
        if (!isDigital(idcard)) {
            return null
        }

        val provinceid = idcard.substring(0, 2)
        // 校验省份
        if (!checkProvinceid(provinceid)) {
            return null
        }

        val birthday = idcard.substring(6, 12)

        val sdf = SimpleDateFormat("yyMMdd")

        var birthdate: Date?
        try {
            birthdate = sdf.parse(birthday)
            val tmpDate = sdf.format(birthdate)
            // 身份证日期错误
            if (tmpDate != birthday) {
                return null
            }

        } catch (e1: ParseException) {
            return null
        }

        val cday = Calendar.getInstance()
        cday.time = birthdate
        val year = cday.get(Calendar.YEAR).toString()

        var idcard17 = idcard.substring(0, 6) + year + idcard.substring(8)

        val c = idcard17.toCharArray()
        var checkCode: String?

        // 将字符数组转为整型数组
        val bit = converCharToInt(c)

        var sum17: Int
        sum17 = getPowerSum(bit)

        // 获取和值与11取模得到余数进行校验码
        checkCode = getCheckCodeBySum(sum17)

        // 获取不到校验位
        if (null == checkCode) {
            return null
        }
        // 将前17位与第18位校验码拼接
        idcard17 += checkCode
        return idcard17
    }

    /**
     * 校验省份
     *
     * @param provinceid
     * @return 合法返回TRUE，否则返回FALSE
     */
    private fun checkProvinceid(provinceid: String): Boolean {
        for (id in cityCode) {
            if (id == provinceid) {
                return true
            }
        }
        return false
    }

    /**
     * 数字验证
     *
     * @param str
     * @return
     */
    private fun isDigital(str: String): Boolean {
        return str.matches("^[0-9]*$".toRegex())
    }

    /**
     * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
     *
     * @param bit
     * @return
     */
    private fun getPowerSum(bit: IntArray): Int {

        var sum = 0

        if (power.size != bit.size) {
            return sum
        }

        for (i in bit.indices) {
            for (j in power.indices) {
                if (i == j) {
                    sum = sum + bit[i] * power[j]
                }
            }
        }
        return sum
    }

    /**
     * 将和值与11取模得到余数进行校验码判断
     *
     * @param checkCode
     * @param sum17
     * @return 校验位
     */
    private fun getCheckCodeBySum(sum17: Int): String? {
        var checkCode: String? = null
        when (sum17 % 11) {
            10 -> checkCode = "2"
            9 -> checkCode = "3"
            8 -> checkCode = "4"
            7 -> checkCode = "5"
            6 -> checkCode = "6"
            5 -> checkCode = "7"
            4 -> checkCode = "8"
            3 -> checkCode = "9"
            2 -> checkCode = "x"
            1 -> checkCode = "0"
            0 -> checkCode = "1"
        }
        return checkCode
    }

    /**
     * 将字符数组转为整型数组
     *
     * @param c
     * @return
     * @throws NumberFormatException
     */
    @Throws(NumberFormatException::class)
    private fun converCharToInt(c: CharArray): IntArray {
        val a = IntArray(c.size)
        var k = 0
        for (temp in c) {
            a[k++] = Integer.parseInt(temp.toString())
        }
        return a
    }

}
