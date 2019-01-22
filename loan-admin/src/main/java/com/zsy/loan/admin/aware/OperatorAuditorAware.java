package com.zsy.loan.admin.aware;

import com.zsy.loan.bean.core.ShiroUser;
import com.zsy.loan.service.shiro.ShiroKit;
import java.util.Optional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

/**
 * Created by Administrator on 2019/1/22/022.
 */
@Configuration
public class OperatorAuditorAware implements AuditorAware<Long> {

  @Override
  public Optional<Long> getCurrentAuditor() {
    ShiroUser user = ShiroKit.getUser();

    if (user == null) {
      return null;
    } else {
      return Optional.ofNullable(user.getId());
    }
  }
}
