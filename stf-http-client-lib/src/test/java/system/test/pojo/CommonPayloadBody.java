package system.test.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.hpinc.jeangiacomin.stf.framework.serialization.SerializeHelper;

public class CommonPayloadBody {

    public String id;
    public String msg;
    public String value;

    public CommonPayloadBody() {
    }

    public CommonPayloadBody(String id, String msg, String value) {
        this.id = id;
        this.msg = msg;
        this.value = value;
    }


    public String serializeContent() throws JsonProcessingException {
        return SerializeHelper.serializeJsonObject(this, JsonInclude.Include.NON_NULL);
    }

    public byte[] extractBytes() throws JsonProcessingException {
        return serializeContent().getBytes();
    }

}
