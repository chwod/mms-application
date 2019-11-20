package com.chwod.robot.action.impl.r.n.uj.d;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chwod.robot.action.ActionService;
import com.chwod.robot.bean.EventContext;
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
	public Sentence process(Sentence sentence, EventContext eventContext) {

		logger.debug("Process class : [{}], sentence : [{}], deep : [{}]", this.getClass().getName(),
				sentence.getRequestWord(), sentence.getProcessDeep());

		String subject = eventContext.getSubject() == null || eventContext.getSubject().size() <= 0 ? null
				: eventContext.getSubject().get(0);

		// no subject
		if (subject == null) {

			eventContext.setType(EventContext.SENTENCE_TYPE_QUESTION);
			eventContext.setFocus(EventContext.SENTENCE_PART_OF_SPEECH_SUBJECT);
			sentence.setResponseWord(new StringBuffer("什么").append(sentence.getRequestWord()).toString());
			return sentence;
		}

		String modificationName = sentence.getPartList().get(1).getWord();

		// get all possible valid solutions
		List<Knowledge> knowledgeList = this.knowledgeService.getAllValidModificationContentSolutions(subject,
				sentence.getPartList().get(3).getWord(), modificationName);
		// not found valid solutions.
		if (knowledgeList == null || knowledgeList.size() <= 0) {
			// get all possible content solutions.
			knowledgeList = this.knowledgeService.getAllPossibleModificationContentSolutions(subject,
					sentence.getPartList().get(3).getWord());
			// not found
			if (knowledgeList == null || knowledgeList.size() <= 0) {
				sentence.setResponseWord("不知道");
				// context setup
				eventContext.setType(EventContext.SENTENCE_TYPE_DECLARATIVE_NEGATIVE);
				eventContext.setFocus(EventContext.SENTENCE_PART_OF_SPEECH_ATTRIBUTIVE_BEFORE_OBJECT);
				return sentence;
			}

			// one knowledge or one of most possible solution
			if (knowledgeList.size() == 1 || (knowledgeList.get(1).getCount() * 2 <= knowledgeList.get(0).getCount())) {

				String content = knowledgeList.get(0).getContent();
				StringBuffer result = new StringBuffer(StringUtils.EMPTY);
				result.append(content).append(",它是").append(modificationName).append("吗?");

				// setup context.
				eventContext.setType(EventContext.SENTENCE_TYPE_QUESTION_YESNO);
				List<String> subjectList = new ArrayList<>();
				subjectList.add(content);
				eventContext.setSubject(subjectList);
				eventContext.setPredicate(EventContext.SENTECNE_PREDICATE_TYPE_IS);
				List<String> objectList = new ArrayList<>();
				objectList.add(modificationName);
				eventContext.setObject(objectList);
				eventContext.setFocus(EventContext.SENTENCE_PART_OF_SPEECH_SUBJECT);

				sentence.setResponseWord(result.toString());
				return sentence;
			}

			// more solutions
			StringBuffer rsult = new StringBuffer(StringUtils.EMPTY);
			List<String> subjectList = new ArrayList<>();
			rsult.append("最有可能的是").append(knowledgeList.get(0).getContent()).append("，但是还有可能是");
			for (int i = 1; i < knowledgeList.size(); i++) {
				Knowledge knowledge = knowledgeList.get(i);
				if (knowledge.getCount() * 2 >= knowledgeList.get(0).getCount()) {
					rsult.append(knowledge.getContent()).append("、");
					subjectList.add(knowledge.getContent());
				}
			}
			rsult.deleteCharAt(rsult.length() - 1);
			rsult.replace(rsult.lastIndexOf("、"), rsult.lastIndexOf("、") + 1, "，或者是");
			rsult.append("，不过我不确定它们是不是你说的").append(modificationName).append("，你能告诉我吗？");

			// setup context.
			eventContext.setType(EventContext.SENTENCE_TYPE_QUESTION_YESNO);
			eventContext.setSubject(subjectList);
			eventContext.setPredicate(EventContext.SENTECNE_PREDICATE_TYPE_IS);
			List<String> attributiveList = new ArrayList<>();
			attributiveList.add(modificationName);
			eventContext.setAttributiveBeforeObject(attributiveList);
			eventContext.setFocus(EventContext.SENTENCE_PART_OF_SPEECH_SUBJECT);

			sentence.setResponseWord(rsult.toString());
			return sentence;
		}

		// only one solution or one most possible solution.
		if ((knowledgeList.size() == 1) || (knowledgeList.get(1).getCount() * 2 <= knowledgeList.get(0).getCount())) {

			String content = knowledgeList.get(0).getContent();

			eventContext.setType(EventContext.SENTENCE_TYPE_DECLARATIVE);
			List<String> attributiveList = new ArrayList<>();
			attributiveList.add(content);
			eventContext.setAttributiveBeforeObject(attributiveList);
			eventContext.setFocus(EventContext.SENTENCE_PART_OF_SPEECH_SUBJECT);
			sentence.setResponseWord(content);
			return sentence;
		}

		// more than one valid solutions.
		List<String> attributeList = new ArrayList<>();
		StringBuffer result = new StringBuffer(StringUtils.EMPTY);

		attributeList.add(knowledgeList.get(0).getContent());
		result.append("最有可能的是").append(knowledgeList.get(0).getContent()).append("，但是还有可能是");
		for (int i = 1; i < knowledgeList.size(); i++) {
			Knowledge knowledge = knowledgeList.get(i);
			if (knowledge.getCount() * 2 >= knowledgeList.get(0).getCount()) {
				result.append(knowledge.getContent()).append("、");
				attributeList.add(knowledge.getContent());
				continue;
			}
			break;
		}
		result.deleteCharAt(result.length() - 1);
		result.replace(result.lastIndexOf("、"), result.lastIndexOf("、") + 1, "，或者是");
		result.append("。");
		attributeList.add(modificationName);

		// context setup
		eventContext.setType(EventContext.SENTENCE_TYPE_DECLARATIVE);
		eventContext.setSubject(attributeList);
		eventContext.setFocus(EventContext.SENTENCE_PART_OF_SPEECH_SUBJECT);

		sentence.setResponseWord(result.toString());
		return sentence;
	}

	@Override
	public void learning(EventContext eventContext, LEARNING flag) {

		logger.debug("Learning class : [{}], sentence : [{}]", this.getClass().getName(),
				eventContext.getCurrentEvent());

		if (Constants.LEARNING.OK == flag) {
			if (eventContext.getType() == EventContext.SENTENCE_TYPE_QUESTION_YESNO) {
				if (eventContext.getSubject() != null && eventContext.getSubject().size() > 0
						&& eventContext.getObject() != null && eventContext.getObject().size() > 0) {
					for (String subject : eventContext.getSubject()) {
						String uuid = UUID.randomUUID().toString();
						for (String object : eventContext.getObject()) {
							Knowledge knowledge = new Knowledge(eventContext.getSessionId());
							knowledge.setEventId(uuid);
							knowledge.setName(object);
							knowledge.setProperty(EventContext.SENTECNE_PREDICATE_TYPE_IS);
							knowledge.setContent(subject);
							this.knowledgeService.save(knowledge);

							knowledge = new Knowledge(eventContext.getSessionId());
							knowledge.setEventId(uuid);
							knowledge.setName(subject);
							knowledge.setProperty(EventContext.SENTECNE_PREDICATE_TYPE_IS);
							knowledge.setContent(object);
							this.knowledgeService.save(knowledge);
						}
					}
					eventContext.setType(null);
				}
			}
		}
	}

}
