package com.zkwg.modelmanager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkwg.modelmanager.entity.*;
import com.zkwg.modelmanager.mapper.*;
import com.zkwg.modelmanager.request.ModelRequest;
import com.zkwg.modelmanager.request.RoleRequest;
import com.zkwg.modelmanager.request.ServerRequest;
import com.zkwg.modelmanager.request.UserRequest;
import com.zkwg.modelmanager.security.dataScope.DataScope;
import com.zkwg.modelmanager.service.IAlgorithmService;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.math.raw.Mod;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelManagerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ModelManagerApplicationTests {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ServerMapper serverMapper;

    @Autowired
    private DictMapper dictMapper;

    @Autowired
    private IAlgorithmService algorithmService;

    @Autowired
    private RoleMapper roleMapper;

    @Test
    void contextLoads() {
        Model model = new Model();
        model.setId(30);
        Model resultModel =  modelMapper.selectOne(new QueryWrapper<>(model));
        System.out.println(resultModel);
        IPage<Model> modelPage = new Page<>(1, 2);//参数一是当前页，参数二是每页个数
        modelPage = modelMapper.selectPage(modelPage,null);
        List<Model> models = modelPage.getRecords();
        models.stream().forEach(m -> System.out.println(m));
    }

    @Test
    void comsumePageTest() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("");
        Page page = new Page();
        IPage<User> userIPage =  userMapper.getUserVosPage(page, userRequest, new DataScope());
        log.info(" userIPage = {}" , userIPage);
    }

    @Test
    void roleMapperTest() {
        RoleRequest roleRequest = new RoleRequest();
        roleRequest.setName("");
        Page page = new Page();
        IPage<Role> roleIPage =  roleMapper.getRolesPage(page, roleRequest, new DataScope());
        log.info(" userIPage = {}" , roleIPage);
    }

    @Test
    void algorithmServiceTest() {
        Algorithm algorithm = algorithmService.selectByPrimaryKey(1);
        log.info(" algorithm = {}" , algorithm);
    }

    @Test
    void dictMapperTest() {
        List<DictItem> dictItems = dictMapper.queryDictItemByDictName("模型类型");
        dictItems.forEach((d) -> log.info(d.toString()));
    }

    @Test
    void subscribeListTest() {
        Page page = new Page();
        ServerRequest serverRequest = new ServerRequest();
        serverRequest.setUserId(1);
//        ModelRequest modelRequest = new ModelRequest();
//        modelRequest.setUserId(1);
//        IPage<Server> serverIPage = serverMapper.subscribeList(page, serverRequest, new DataScope());
//        serverIPage.getRecords().forEach(server -> server.toString());

//        IPage<Model> modelIPage = modelMapper.subscribeList(page, modelRequest, new DataScope());
//        modelIPage.getRecords().forEach(model -> log.info(model.toString()));

        IPage<Server> serverIPage = serverMapper.publicServerList(page, serverRequest);
        serverIPage.getRecords().forEach(server -> server.toString());
    }


    @Test
    void updateModelByUrlTest() {
        Model model = new Model();
        model.setMinioUrl("s3://sklearn-model/2020-09-25--16:12:22");
        model.setStatus((byte) 2);

        List<Model> models = new ArrayList<>();
        models.add(model);

        modelMapper.updateStatusByUrls(models);
    }

}
