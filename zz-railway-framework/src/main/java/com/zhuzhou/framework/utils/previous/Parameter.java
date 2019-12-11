package com.zhuzhou.framework.utils.previous;

public class Parameter {

   public ResponseInfo deleteagInfo(Integer r_id, Integer t_id) {
       ResponseInfo resp = new ResponseInfo();
       if (r_id == null || "".equals(r_id)) {
           resp.setCodeAndDesc(CloudResultCode.INITG, "参数taskName不能为空");
           return resp;
       }
       if (t_id == null || "".equals(t_id)) {
           resp.setCodeAndDesc(CloudResultCode.INITG, "参数taskName不能为空");
           return resp;
       }
       return resp;
   }
}
