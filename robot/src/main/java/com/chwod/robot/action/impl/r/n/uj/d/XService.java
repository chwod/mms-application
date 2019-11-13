package com.chwod.robot.action.impl.r.n.uj.d;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chwod.robot.action.ActionService;
import com.chwod.robot.bean.Event;
import com.chwod.robot.bean.Sentence;
import com.chwod.robot.domain.Knowledge;
import com.chwod.robot.service.KnowledgeService;
import com.chwod.robot.utils.Constants;
import com.chwod.robot.utils.Constants.LEARNING;

/**
 * @implSpec 哪个国家的首都？
 * @author liuxun
 *
 */
@Component("SS:R-N-UJ-D-X")
public class XService implements ActionService {

	private static final Logger logger = LoggerFactory.getLogger(ActionService.class);

	@Autowired
	private KnowledgeService knowledgeService;

	@Override
	public Sentence process(Sentence sentence) {

		logger.debug("Process class : [{}], sentence : [{}], deep : [{}]", this.getClass().getName(),
				sentence.getRequestWord(), sentence.getProcessDeep());

		HttpSession session = sentence.getSession();
		Event event = session.getAttribute(Constants.CONTEXT_EVENT) == null ? new Event()
				: (Event) session.getAttribute(Constants.CONTEXT_EVENT);
		String subject = event.getSubject();

		// no subject
		if (subject == null) {
			sentence.setResponseWord(new StringBuffer("什么").append(sentence.getRequestWord()).toString());
			return sentence;
		}

		String relationProperty = sentence.getPartList().get(1).getWord();

		// get all possible valid solutions
		List<Knowledge> knowledgeList = this.knowledgeService.getAllValidContentSolutions(subject,
				sentence.getPartList().get(3).getWord(), relationProperty);
		// not found valid solutions.
		if (knowledgeList == null || knowledgeList.size() <= 0) {
			// get all possible content solutions.
			knowledgeList = this.knowledgeService.getAllPossibleContentSolutions(subject,
					sentence.getPartList().get(3).getWord());
			// not found
			if (knowledgeList == null || knowledgeList.size() <= 0) {
				sentence.setResponseWord("不知道");
				return sentence;
			}

			// one knowledge or one of most possible solution
			if (knowledgeList.size() == 1 || (knowledgeList.get(1).getCount() * 2 <= knowledgeList.get(0).getCount())) {

				String answer = knowledgeList.get(0).getContent();
				StringBuffer result = new StringBuffer(StringUtils.EMPTY);
				result.append(answer).append(",它是").append(relationProperty).append("吗?");
				// setup context.
				List<String> currentEventList = new ArrayList<>();
				currentEventList.add(sentence.getRequestWord());
				session.setAttribute(Constants.CURRENT_CONTEXT_EVENTS, currentEventList);
				List<String> issuesList = new ArrayList<>();
				issuesList.add(answer);
				issuesList.add(relationProperty);
				session.setAttribute(Constants.CURRENT_CONTEXT_STATUS_MISSING_RELATIONSHIP_IS, issuesList);
				sentence.setResponseWord(result.toString());
				return sentence;
			}

			// more solutions
			StringBuffer answer = new StringBuffer(StringUtils.EMPTY);
			List<String> issuesList = new ArrayList<>();
			answer.append("最有可能的是").append(knowledgeList.get(0).getContent()).append("，但是还有可能是");
			for (int i = 1; i < knowledgeList.size(); i++) {
				Knowledge knowledge = knowledgeList.get(i);
				if (knowledge.getCount() * 2 > knowledgeList.get(0).getCount()) {
					answer.append(knowledge.getContent()).append("、");
					issuesList.add(knowledge.getContent());
				}
			}
			answer.deleteCharAt(answer.length() - 1);
			answer.replace(answer.lastIndexOf("、"), answer.lastIndexOf("、") + 1, "，或者是");
			answer.append("，不过我不确定它们是不是").append(relationProperty).append("，你能告诉我吗？");
			// setup context.
			List<String> currentEventList = new ArrayList<>();
			currentEventList.add(sentence.getRequestWord());
			issuesList.add(relationProperty);
			session.setAttribute(Constants.CURRENT_CONTEXT_EVENTS, currentEventList);
			session.setAttribute(Constants.CURRENT_CONTEXT_STATUS_MISSING_RELATIONSHIP_IS, issuesList);
			sentence.setResponseWord(answer.toString());
			return sentence;
		}

		// only one solution or one most possible solution.
		if ((knowledgeList.size() == 1) || (knowledgeList.get(1).getCount() * 2 <= knowledgeList.get(0).getCount())) {
			sentence.setResponseWord(knowledgeList.get(0).getContent());
			return sentence;
		}

		// more than one valid solutions.
		StringBuffer answer = new StringBuffer(StringUtils.EMPTY);
		answer.append("最有可能的是").append(knowledgeList.get(0).getContent()).append("，但是还有可能是");
		for (int i = 1; i < knowledgeList.size(); i++) {
			Knowledge knowledge = knowledgeList.get(i);
			if (knowledge.getCount() * 2 > knowledgeList.get(0).getCount()) {
				answer.append(knowledge.getContent()).append("、");
				continue;
			}
			break;
		}
		answer.deleteCharAt(answer.length() - 1);
		answer.replace(answer.lastIndexOf("、"), answer.lastIndexOf("、") + 1, "，或者是");
		answer.append("。");
		sentence.setResponseWord(answer.toString());
		return sentence;
	}

	@Override
	public void learning(Sentence sentence, LEARNING flag) {
		// TODO Auto-generated method stub

	}

}
