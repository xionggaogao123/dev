package com.fulaan.learningcenter.dto;

import com.pojo.school.InteractLessonExamDetailEntry;

/**
 * Created by guojing on 2015/12/3.
 */
public class InteractLessonExamDetailDTO {
    private String id;

    private String lessonId;

    private String examId;

    private String userId;

    private String userName;

    private int times;

    private String examName;

    private int number;

    private String format;

    private String formatDes;

    private int answer;

    private String answerDes;

    private String answerFilePath;

    private int correct;

    private String correctDes;

    private int result;

    private String question;

    public InteractLessonExamDetailDTO(){

    }

    public InteractLessonExamDetailDTO(InteractLessonExamDetailEntry entry) {
        if(entry !=null) {
            this.id=entry.getID().toString();

            this.lessonId=entry.getLessonId()==null?"":entry.getLessonId().toString();

            this.examId=entry.getExamId()==null?"":entry.getExamId().toString();

            this.userId=entry.getUserId()==null?"":entry.getUserId().toString();

            this.times=entry.getTimes();

            this.examName=entry.getExamName();

            this.number=entry.getNumber();

            this.format=entry.getFormat();

            this.formatDes=entry.getFormatDes();

            this.answer=entry.getAnswer();

            this.answerDes=entry.getAnswerDes();

            this.correct=entry.getCorrect();

            this.correctDes=entry.getCorrectDes();

            this.result=entry.getResult();

            this.question=entry.getQuestion();

        }else{
            new InteractLessonExamDetailDTO();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFormatDes() {
        return formatDes;
    }

    public void setFormatDes(String formatDes) {
        this.formatDes = formatDes;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public String getAnswerDes() {
        return answerDes;
    }

    public void setAnswerDes(String answerDes) {
        this.answerDes = answerDes;
    }

    public String getAnswerFilePath() {
        return answerFilePath;
    }

    public void setAnswerFilePath(String answerFilePath) {
        this.answerFilePath = answerFilePath;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public String getCorrectDes() {
        return correctDes;
    }

    public void setCorrectDes(String correctDes) {
        this.correctDes = correctDes;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
