package li.entrix.translate;

public enum LanguageCode {

    EN("en"), RU("ru"), CN("zh-CN"), JP("ja");

    private String code;

    LanguageCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
