import static org.junit.Assert.*;

import org.junit.Test;

import com.zsz.front.utils.RupengSMSAPI;
import com.zsz.front.utils.RupengSMSResult;

public class testSend {

	@Test
	public void test() {
		RupengSMSResult r1= RupengSMSAPI.send("123456", "18888886666");
	   assertNotNull(r1);
	   assertEquals(r1.getCode(), 0);
	  
	}

}
