public class RequestCloudDto extends RequestDTO{

    public final boolean isWeb;
    public RequestCloudDto(String authenticatiorId,boolean isWeb) {
        super(authenticatiorId);

        this.isWeb = isWeb;
    }
}
