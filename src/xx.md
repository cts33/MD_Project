# 1.sdfds
sgs

```java


    public static CommonParams getCommonParams(String baseUrl) {
        if (TextUtils.isEmpty(baseUrl)) {
            throw new NullPointerException("url is empty");
        }
        CommonParams commonParams = commonParamsMap.get(baseUrl);
        if (commonParams == null) {
            commonParams = new CommonParams();
            commonParamsMap.put(baseUrl, commonParams);
        }
        return commonParams;
    }

```