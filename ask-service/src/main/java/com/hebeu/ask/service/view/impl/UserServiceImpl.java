package com.hebeu.ask.service.view.impl;

import com.github.pagehelper.PageHelper;
import com.hebeu.ask.dao.AttentionMapper;
import com.hebeu.ask.dao.DoingMapper;
import com.hebeu.ask.dao.UserDataMapper;
import com.hebeu.ask.dao.UserMapper;
import com.hebeu.ask.model.po.*;
import com.hebeu.ask.model.vo.UserTop;
import com.hebeu.ask.service.view.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : chenDeHua
 * Time   : 2018/5/10 下午6:29
 * Desc   :
 **/

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDataMapper userDataMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DoingMapper doingMapper;

    @Autowired
    private AttentionMapper attentionMapper;

    /**
     * 用户排行榜
     * @param queryNum 查询数量
     * @return 返回用户排行榜
     */
    @Override
    public List<UserTop> queryUserTop(Integer queryNum, String topType) {
        if ("coins".equals(topType)){
            PageHelper.startPage(1, queryNum, "coins desc");
        }else if ("credits".equals(topType)){
            PageHelper.startPage(1, queryNum, "credits desc");
        }
        UserDataExample userDataExample = new UserDataExample();
        UserDataExample.Criteria criteria = userDataExample.createCriteria();
        criteria.andUserIdIsNotNull();
        List<UserData> userDataList = userDataMapper.selectByExample(userDataExample);

        List<UserTop> userTops = new ArrayList<>();
        userDataList.iterator().forEachRemaining(userData -> {
            User user = userMapper.selectByPrimaryKey(userData.getUserId());
            UserTop coinTop = new UserTop();
            coinTop.setUserData(userData);
            coinTop.setUser(user);
            userTops.add(coinTop);
        });

        return userTops;
    }

    /**
     * 根据用户id查询动态信息
     *
     * @param userId 用户id
     * @return 返回动态集合
     */
    @Override
    public List<Doing> queryDoingByUserId(Integer userId) {
        DoingExample doingExample = new DoingExample();
        DoingExample.Criteria criteria = doingExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        return doingMapper.selectByExample(doingExample);
    }

    /**
     * 根据用户id查询用户信息
     *
     * @param userId 用户id
     * @return 返回用户信息
     */
    @Override
    public User queryUserById(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    /**
     * 根据用户id查询用户信息
     *
     * @param userId 用户id
     * @return 返回用户数据
     */
    @Override
    public UserData queryUserDataByUserId(Integer userId) {
        return userDataMapper.selectByPrimaryKey(userId);
    }

    /**
     * 更新用户数据
     *
     * @param userData
     */
    @Override
    public void updateUserData(UserData userData) {
        userData.setViews(userData.getViews() +1);
        userDataMapper.updateByPrimaryKeySelective(userData);
    }

    /**
     * 关注用户
     *
     * @param sourceId 被关注用户id
     * @param userId   关注用户id
     */
    @Override
    public void followUser(Integer sourceId, Integer userId) {
        Attention attention = new Attention();
        attention.setUserId(userId);
        attention.setSourceId(sourceId);
        attention.setSourceType("user");

        UserData userData = userDataMapper.selectByPrimaryKey(sourceId);
        userData.setFollowers(userData.getFollowers() +1);
        userDataMapper.updateByPrimaryKeySelective(userData);

        attentionMapper.insertSelective(attention);
    }

    /**
     * 查询关注用户
     *
     * @param userId   用户id
     * @param sourceId 源用户id
     * @return 返回关注用户集合
     */
    @Override
    public List<Attention> queryAttention(Integer userId, Integer sourceId) {
        AttentionExample attentionExample = new AttentionExample();
        AttentionExample.Criteria criteria = attentionExample.createCriteria();
        if (userId != null){
            criteria.andUserIdEqualTo(userId);
        }
        if (sourceId != null){
            criteria.andSourceIdEqualTo(sourceId);
        }
        criteria.andSourceTypeEqualTo("user");
        return attentionMapper.selectByExample(attentionExample);
    }


}
