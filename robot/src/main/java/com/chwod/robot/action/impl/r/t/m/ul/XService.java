package com.chwod.robot.action.impl.r.t.m.ul;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chwod.robot.action.ActionService;
import com.chwod.robot.bean.EventContext;
import com.chwod.robot.bean.Sentence;
import com.chwod.robot.component.SelfInfoComponent;
import com.chwod.robot.utils.Constants.LEARNING;

/**
 * @implSpec 你今年几岁了?
 */

@Component("SS:R-T-M-UL-X")
public class XService implements ActionService {

	private static final Logger logger = LoggerFactory.getLogger(ActionService.class);

	private static final Long YEARTIME = 1000L * 60 * 60 * 24 * 365;
	private static final Long MONTHTIME = 1000L * 60 * 60 * 24 * 30;
	private static final Long DAYTIME = 1000L * 60 * 60 * 24;

	@Autowired
	private SelfInfoComponent selfInfoComponent;

	@Override
	public Sentence process(Sentence sentence, EventContext eventContext) {

		logger.debug("Process class : [{}], sentence : [{}], deep : [{}]", this.getClass().getName(), sentence.getRequestWord(),
				sentence.getProcessDeep());
		
		Calendar calendar = Calendar.getInstance();
		Long currentTime = calendar.getTimeInMillis();

		Calendar birthdayCalendar = this.selfInfoComponent.getBirthday();
		Long birthdayTime = birthdayCalendar.getTimeInMillis();

		Long compareTime = currentTime - birthdayTime;

		if (compareTime >= YEARTIME) {
			Long age = compareTime / YEARTIME;
			sentence.setResponseWord(new StringBuffer("今年").append(age).append("岁了").toString());
		}
		if (compareTime < YEARTIME) {
			sentence.setResponseWord("还不到一岁");
		}
		if (compareTime < MONTHTIME) {
			sentence.setResponseWord("几天而已");
		}
		if (compareTime < DAYTIME) {
			sentence.setResponseWord("不到一天");
		}

		eventContext.setType(EventContext.SENTENCE_TYPE_DECLARATIVE);
		
		return sentence;

	}

	@Override
	public void learning(EventContext eventContext, LEARNING flag) {
		logger.debug("Learning class : [{}], sentence : [{}]", this.getClass().getName(),
				eventContext.getCurrentEvent());		
	}
}
