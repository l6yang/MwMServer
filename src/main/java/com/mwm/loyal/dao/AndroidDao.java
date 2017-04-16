package com.mwm.loyal.dao;

import com.mwm.loyal.model.AccountBean;
import com.mwm.loyal.model.FeedBackBean;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.List;

public class AndroidDao implements BaseAndroidDao {
    private SqlSessionTemplate sessionTemplate;

    @Override
    public AccountBean loginByAccount(AccountBean accountBean) {
        return sessionTemplate.selectOne("loginByAccount", accountBean);
    }

    @Override
    public AccountBean getUserByAccount(AccountBean accountBean) {
        return sessionTemplate.selectOne("getUserByAccount", accountBean);
    }

    @Override
    public int registerAccount(AccountBean accountBean) {
        return sessionTemplate.insert("registerAccount", accountBean);
    }

    @Override
    public int updateAccount(AccountBean bean) {
        return sessionTemplate.update("updateAccount", bean);
    }

    @Override
    public int destroyAccount(AccountBean bean) {
        return sessionTemplate.delete("destroyAccount", bean);
    }

    @Override
    public List<FeedBackBean> queryAll() {
        return sessionTemplate.selectList("queryAll");
    }

    @Override
    public List<FeedBackBean> queryByAccount(String account) {
        return sessionTemplate.selectList("queryByAccount", account);
    }

    @Override
    public int insertFeedBack(FeedBackBean feedBackBean) {
        return sessionTemplate.insert("insertFeedBack", feedBackBean);
    }

    @Override
    public int deleteFeedBack(FeedBackBean feedBackBean) {
        return sessionTemplate.delete("deleteFeedBack", feedBackBean);
    }

    public void setSessionTemplate(SqlSessionTemplate sessionTemplate) {
        this.sessionTemplate = sessionTemplate;
    }

    public SqlSessionTemplate getSessionTemplate() {
        return sessionTemplate;
    }
}
