public class Source {
    public static final String contentUrl = "http://maths.tju.edu.cn/szdw/jsdw.htm";
    private static final String teacherName = "宋先发李德生刘志新宋占杰陈鑫邓英俊张勇魏斌侯庆虎赵铭锋蒋仁进郑有泉黄正海甘在会吴奕飞郭嘉祥吴华明韩忠杰董兴堂孙晓晨胡玉梅王健波吕良福";

    /**
     * @param str the tested string
     * @return return true when this string is the substring of {@link Source#teacherName}
     */
    public static boolean isInList(CharSequence str) {
        return teacherName.contains(str);
    }
}
