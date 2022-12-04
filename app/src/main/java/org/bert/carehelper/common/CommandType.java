package org.bert.carehelper.common;

/**
 * 指令类型
 */
public class CommandType {
    // ========= 文件读取相关能力 ===========
    public static final String FILE = "file";
    // 读取QQ文件
    public static final String QQ = "qq";
    // 读取微信文件
    public static final String WE = "we";
    // ========= 位置模块相关能力 ===========
    public static final String LOCATION = "location";
    public static final String UPDATE = "update";
    public static final String CANCEL = "cancel";

    // ========= 手机模块相关能力 ===========
    public static final String PHONE = "phone";
    // 拍照
    public static final String TAKE_PHOTOS = "takePhotos";
    // 录音
    public static final String VOICES = "VOICES";

    public static final String MAX_VOLUM = "setMaxVolum";

    public static final String CONTACT_LIST= "ContactList";

    public static final String CALL_PHONE = "callPhone";

    public static final String PHONE_RECORDS = "PhoneRecords";

    public static final String PHONE_MESSAGE_LIST = "PhoneMessageList";

}
