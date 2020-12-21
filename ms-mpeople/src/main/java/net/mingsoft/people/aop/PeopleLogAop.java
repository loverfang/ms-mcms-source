package net.mingsoft.people.aop;

import net.mingsoft.basic.annotation.LogAnn;
import net.mingsoft.basic.aop.BaseLogAop;
import net.mingsoft.basic.constant.e.OperatorTypeEnum;
import net.mingsoft.basic.constant.e.SessionConstEnum;
import net.mingsoft.basic.entity.ManagerSessionEntity;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.people.entity.PeopleEntity;
import org.springframework.stereotype.Component;

/**
 * @author by 铭飞开源团队
 * @Description TODO
 * @date 2019/11/20 10:28
 */
@Component
public class PeopleLogAop extends BaseLogAop{


    @Override
    public String getUserName() {
        PeopleEntity peopleEntity = (PeopleEntity)BasicUtil.getSession(net.mingsoft.people.constant.e.SessionConstEnum.PEOPLE_SESSION);
        //会员用户获取用户名
        return peopleEntity.getPeopleUser().getPuNickname();
    }

    @Override
    public boolean isCut(LogAnn log) {
        //只有会员用户操作才走这个AOP
        return log.operatorType() == OperatorTypeEnum.PEOPLE;
    }
}
