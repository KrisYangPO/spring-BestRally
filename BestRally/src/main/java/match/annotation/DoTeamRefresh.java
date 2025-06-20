package match.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // 可以作用在方法上。
@Retention(RetentionPolicy.RUNTIME) // 表示在執行期(Runtime)也能讀取到，AOP 需要。
public @interface DoTeamRefresh {
	
	// 定義一個註解屬性：teamIdParam，用來標示 teamId 的參數名或路徑
	// 所以在 Service 方法使用這個自訂註解時，要寫：@doTeamRefresh(teamIdParam = "??參數??")
	// 告訴 Aspect 說，參數是來自 teamIdParam，並且由他的值來找到 teamId 執行 teamRefresh 方法。
    String teamIdParam(); 
}
