package com.chwod.robot.action.impl.v.ns.v.r.y;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chwod.robot.action.ActionService;
import com.chwod.robot.bean.Event;
import com.chwod.robot.bean.Part;
import com.chwod.robot.bean.Sentence;
import com.chwod.robot.domain.Experience;
import com.chwod.robot.domain.Knowledge;
import com.chwod.robot.service.ExperienceService;
import com.chwod.robot.service.KnowledgeService;
import com.chwod.robot.service.TalkService;
import com.chwod.robot.utils.Constants;
import com.chwod.robot.utils.Constants.LEARNING;

/**
 * @implSpec 知道北京是什么吗？
 * @author liuxun
 *
 */
@Component("SS:V-NS-V-R-Y-X")
public class XService implements ActionService {

	private static final Logger logger = LoggerFactory.getLogger(ActionService.class);

	@Autowired
	private KnowledgeService knowledgeService;

	@Autowired
	private TalkService talkService;

	@Autowired
	private ExperienceService experienceService;

	@Override
	public Sentence process(Sentence sentence) {

		logger.debug("Process class : [{}], sentence : [{}], deep : [{}]", this.getClass().getName(),
				sentence.getRequestWord(), sentence.getProcessDeep());

		// set subject in context.
		HttpSession session = sentence.getSession();
		Event event = session.getAttribute(Constants.CONTEXT_EVENT) == null ? new Event()
				: (Event) session.getAttribute(Constants.CONTEXT_EVENT);
		event.setSubject(sentence.getPartList().get(1).getWord());

		List<Knowledge> knowledgeList = this.knowledgeService.getKnowSolutions(sentence.getPartList().get(1).getWord(),
				Constants.IS);

		// unknown
		if (knowledgeList == null || knowledgeList.size() <= 0) {
			sentence.setResponseWord("不知道");
			return sentence;
		}

		// one solution
		if (knowledgeList.size() == 1) {
			sentence.setResponseWord(new StringBuffer("知道,").append(sentence.getPartList().get(1).getWord()).append("是")
					.append(knowledgeList.get(0).getContent()).toString());
			return sentence;
		}

		// more than one solution
		StringBuffer responseWords = new StringBuffer("知道,").append(sentence.getPartList().get(1).getWord())
				.append("是");
		for (int i = 0; i < knowledgeList.size(); i++) {
			responseWords.append(knowledgeList.get(i).getContent());
			if (i < knowledgeList.size() - 1) {
				responseWords.append(", 也是");
			}
		}
		responseWords.append("。");
		sentence.setResponseWord(responseWords.toString());
		return sentence;
	}

	@Override
	public void learning(Sentence sentence, LEARNING flag) {
		if (Constants.LEARNING.OK == flag) {
			if (sentence.getSession().getAttribute(Constants.CONTEXT_EVENT) == null) {
				return;
			}
			if (sentence.getPartList() == null) {
				sentence.setPartList(this.talkService.parse(sentence.getRequestWord()));
			}
			Event event = (Event) sentence.getSession().getAttribute(Constants.CONTEXT_EVENT);
			String subject = event.getSubject();
			if (StringUtils.isNotBlank(subject)) {
				List<Part> partList = sentence.getPartList();
				for (int i = 0; i < partList.size(); i++) {
					if (subject.equals(partList.get(i).getWord())) {
						Experience experience = new Experience();
						experience.setParseWord(sentence.getParseWord());
						experience.setProperty(Constants.SUBJECT);
						experience.setContent(String.valueOf(i));
						this.experienceService.save(experience);
						break;
					}
				}
			}
		}
	}

}
