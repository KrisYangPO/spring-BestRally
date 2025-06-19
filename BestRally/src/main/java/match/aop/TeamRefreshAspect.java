package match.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import match.service.TeamRefreshDataService;

@Component
@Aspect
public class TeamRefreshAspect {
	
	@Autowired
	private TeamRefreshDataService teamRefreshDataService;
	
	// 書寫需要執行 teamRefreshDataService 的主程式
	@Pointcut(value = "execution(public void match.service.impl.PlayerServiceImpl.updatePlayerLevel(Integer, Integer))")
	public void updatePlayerLevel() {};
	
	
	// 執行完 updatePlayerLevel 就要執行 TeamRefreshService
	@AfterReturning(value = "updatePlayerLevel()")
	public void AfterPlayerLevelUpdate(JoinPoint joinPoint) {
		String methodName = joinPoint.getSignature().getName();
		
	}
}
