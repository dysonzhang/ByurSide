package cn.eoe.app.biz;

import android.app.Activity;
import org.codehaus.jackson.map.ObjectMapper;

public class BaseDao {
	protected Activity mActivity;
	ObjectMapper mObjectMapper = new ObjectMapper();

	public BaseDao() {
	}

	public BaseDao(Activity paramActivity) {
		this.mActivity = paramActivity;
	}
}