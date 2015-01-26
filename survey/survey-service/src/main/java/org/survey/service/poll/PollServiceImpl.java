package org.survey.service.poll;

import java.util.List;

import javax.annotation.Resource;
import javax.jws.WebService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.Validate;
import org.survey.model.poll.Poll;
import org.survey.model.poll.Question;
import org.survey.model.user.User;
import org.survey.repository.poll.PollRepository;
import org.survey.repository.poll.QuestionRepository;
import org.survey.repository.user.UserRepository;

import com.google.common.collect.Iterables;

/**
 * Implementation of PollService. endpointInterface and serviceName are probably
 * unneccessary.
 */
@WebService(endpointInterface = "org.survey.service.poll.PollService", serviceName = "pollService")
public class PollServiceImpl implements PollService {
    @Resource
    private UserRepository userRepository;
    @Resource
    private PollRepository pollRepository;
    @Resource
    private QuestionRepository questionRepository;
    private static final Poll[] EMPTY_POLL_ARRAY = new Poll[0];

    @Override
    public Poll[] findAll() {
        Iterable<Poll> polls = pollRepository.findAll();
        // return empty list instead of null
        if (Iterables.isEmpty(polls)) {
            return EMPTY_POLL_ARRAY;
        } else {
            return Iterables.toArray(polls, Poll.class);
        }
    }

    public Poll[] findByOwner(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return EMPTY_POLL_ARRAY;
        }
        Iterable<Poll> polls = pollRepository.findAllByOwner(user);
        // return empty list instead of null
        if (Iterables.isEmpty(polls)) {
            return EMPTY_POLL_ARRAY;
        } else {
            return Iterables.toArray(polls, Poll.class);
        }
    }

    @Override
    public Poll create(Poll poll) {
        Validate.notNull(poll);
        Validate.isTrue(!exists(poll.getName()), "Poll already exists: {}", poll.getName());
        return update(poll);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Poll update(Poll poll) {
        Poll savedPoll = pollRepository.save(poll);
        if (CollectionUtils.isNotEmpty(poll.getQuestions())) {
            for (Question question : poll.getQuestions()) {
                question.setPoll(savedPoll);
            }
            Iterable<Question> savedQuestions = questionRepository.save(poll.getQuestions());
            poll.setQuestions(IteratorUtils.toList(savedQuestions.iterator()));
        }
        return savedPoll;
    }

    @Override
    // TODO change to id
    public Poll findOne(String username) {
        return pollRepository.findByName(username);
    }

    @Override
    public boolean exists(String username) {
        return pollRepository.findByName(username) != null;
    }

    @Override
    public void delete(String name) {
        Poll poll = pollRepository.findByName(name);
        if (poll == null) {
            return;
        }
        List<Question> questions = questionRepository.findAllByPoll(poll);
        questionRepository.delete(questions);
        pollRepository.delete(poll.getId());
    }

    @Override
    public long count() {
        return pollRepository.count();
    }
}