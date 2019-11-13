package com.chwod.robot.component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chwod.robot.domain.Configuration;
import com.chwod.robot.service.ConfigurationService;
import com.chwod.robot.utils.Constants;

@Component
public class SelfInfoComponent {

	private static final Logger logger = LoggerFactory.getLogger(SelfInfoComponent.class);

	@Autowired
	private ConfigurationService configurationService;

	private static final Object LOGIC = new Object();
	private static final int DEFAULT_YEAR = 2017;
	private static final int DEFAULT_MONTH = 10;
	private static final int DEFAULT_DAY = 1;
	private static Calendar birthday;

	/**
	 * get birthday of robot self.
	 * 
	 * @return
	 */
	public Calendar getBirthday() {
		if (birthday == null) {
			synchronized (LOGIC) {
				if (birthday == null) {
					List<Configuration> configurationList = this.configurationService.getByName(Constants.ROBOT_SELF);
					int year = DEFAULT_YEAR;
					int month = DEFAULT_MONTH;
					int day = DEFAULT_DAY;
					if (configurationList != null && configurationList.size() > 0) {
						for (Configuration configuration : configurationList) {
							if (configuration.getTitle().equalsIgnoreCase(Constants.BIRTHDAY_YEAR)) {
								year = configuration.getVal() == null ? DEFAULT_YEAR
										: configuration.getVal().intValue();
							}
							if (configuration.getTitle().equalsIgnoreCase(Constants.BIRTHDAY_MONTH)) {
								month = configuration.getVal() == null ? DEFAULT_MONTH
										: configuration.getVal().intValue();
							}
							if (configuration.getTitle().equalsIgnoreCase(Constants.BIRTHDAY_DAY)) {
								day = configuration.getVal() == null ? DEFAULT_DAY : configuration.getVal().intValue();
							}
						}
					}

					birthday = Calendar.getInstance();
					birthday.set(year, month, day, 0, 0, 0);
					logger.debug("get birthday : {}", new SimpleDateFormat("yyyy-MM-dd").format(birthday.getTime()));
				}
			}
		}

		return birthday;
	}
}
