package com.teach.javafxclient.controller;

import com.teach.javafxclient.MainApplication;
import com.teach.javafxclient.controller.admin.AddStudentController;
import com.teach.javafxclient.controller.admin.FilterStudentController;
import com.teach.javafxclient.controller.base.LocalDateStringConverter;
import com.teach.javafxclient.controller.base.ToolController;
import com.teach.javafxclient.model.StudentEntity;
import com.teach.javafxclient.request.*;
import com.teach.javafxclient.util.CommonMethod;
import com.teach.javafxclient.controller.base.MessageDialog;
import com.teach.javafxclient.util.DialogUtil;
import com.teach.javafxclient.util.LocalDateUtil;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.font.FontResources;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import atlantafx.base.theme.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * StudentController 登录交互控制类 对应 student_panel.fxml  对应于学生管理的后台业务处理的控制器，主要获取数据和保存数据的方法不同
 *  @FXML  属性 对应fxml文件中的
 *  @FXML 方法 对应于fxml文件中的 on***Click的属性
 */
public class StudentController extends ToolController {
    public FlowPane filterPane;
    public MFXButton addFilterButton;
    public MFXButton changeFilterButton;
    public MFXButton resetFilterButton;
    public Label filterLabel;
    public MFXButton addButton;
    public MFXButton deleteButton;
    public MFXButton getExcelButton;
    public MFXButton importExcelButton;
    @FXML
    private TableView<StudentEntity> dataTableView;  //学生信息表
    @FXML
    public TableColumn<StudentEntity,Boolean> checkColumn;
    @FXML
    private TableColumn<StudentEntity,String> numColumn;   //学生信息表 编号列
    @FXML
    private TableColumn<StudentEntity,String> nameColumn; //学生信息表 名称列
    @FXML
    private TableColumn<StudentEntity,String> deptColumn;  //学生信息表 院系列
    @FXML
    private TableColumn<StudentEntity,String> majorColumn; //学生信息表 专业列
    @FXML
    private TableColumn<StudentEntity,String> classNameColumn; //学生信息表 班级列
    @FXML
    private TableColumn<StudentEntity,String> cardColumn; //学生信息表 证件号码列
    @FXML
    private TableColumn<StudentEntity,String> genderColumn; //学生信息表 性别列
    @FXML
    private TableColumn<StudentEntity,String> birthdayColumn; //学生信息表 出生日期列
    @FXML
    private TableColumn<StudentEntity,String> emailColumn; //学生信息表 邮箱列
    @FXML
    private TableColumn<StudentEntity,String> phoneColumn; //学生信息表 电话列
    @FXML
    private TableColumn<StudentEntity,String> addressColumn;//学生信息表 地址列

    @FXML
    private TextField numField; //学生信息  学号输入域
    @FXML
    private TextField nameField;  //学生信息  名称输入域
    @FXML
    private TextField deptField; //学生信息  院系输入域
    @FXML
    private TextField majorField; //学生信息  专业输入域
    @FXML
    private TextField classNameField; //学生信息  班级输入域
    @FXML
    private TextField cardField; //学生信息  证件号码输入域
    @FXML
    private ComboBox<OptionItem> genderComboBox;  //学生信息  性别输入域
    @FXML
    private DatePicker birthdayPick;  //学生信息  出生日期选择域
    @FXML
    private TextField emailField;  //学生信息  邮箱输入域
    @FXML
    private TextField phoneField;   //学生信息  电话输入域
    @FXML
    private TextField addressField;  //学生信息  地址输入域

    @FXML
    private TextField numNameTextField;  //查询 姓名学号输入域

    private Integer studentId = null;  //当前编辑修改的学生的主键

    private ArrayList<StudentEntity> studentList = new ArrayList<StudentEntity>();  // 学生信息列表数据
    private List<OptionItem> genderList;   //性别选择列表数据
    private ObservableList<StudentEntity> observableList = FXCollections.observableArrayList();  // TableView渲染列表

    private List<StudentEntity> selectedItemList = null;

    private HttpRequestUtil<StudentEntity> httpRequestUtil = new HttpRequestUtil<>(StudentEntity.class);

    private final DialogUtil dialogUtil = new DialogUtil();

    //存储筛选的条件
    private StudentEntity filterCriteria = new StudentEntity();

    /**
     * 页面加载对象创建完成初始化方法，页面中控件属性的设置，初始数据显示等初始操作都在这里完成，其他代码都事件处理方法里
     */

    @FXML
    public void initialize() throws InvocationTargetException, IllegalAccessException {
        DataResponse<ArrayList<StudentEntity>> res;
        DataRequest req =new DataRequest();
        req.put("numName","");
        res = httpRequestUtil.requestArrayList("/api/student/getStudentList",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            studentList = res.getData();
        }

        //设置表的列属性和表属性
        setupTable();

        //初始化右边栏元素
        genderList = HttpRequestUtil.getDictionaryOptionItemList("XBM");
        genderComboBox.getItems().addAll(genderList);
        birthdayPick.setConverter(new LocalDateStringConverter("yyyy-MM-dd"));

        //初始化筛选器及其按钮
        resetFilter();
    }

    /**
     * 将学生数据集合设置到面板上显示
     */
    private void setTableViewData() {
        observableList.clear();
        observableList.addAll(FXCollections.<StudentEntity>observableArrayList(studentList));
        dataTableView.setItems(observableList);
    }

    /**
     * 初始化表格列元素属性
     */
    private void setupTable(){

        var selectAll = new CheckBox();

        //创建复选框列的全选按钮并放入column中
        checkColumn.setGraphic(selectAll);
        checkColumn.setSortable(false);//复选框列不可点击列顶排序
        //设置列元素为学生对象的checkColumn成员
        checkColumn.setCellValueFactory(c -> c.getValue().selectProperty());
/*        checkColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<StudentTableEntity, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<StudentTableEntity, Boolean> param) {
                StudentTableEntity studentTableEntity = param.getValue();
                Boolean value = studentTableEntity.getSelect();
                SimpleBooleanProperty observableValue = new SimpleBooleanProperty(value);
                return observableValue;
            }
        });*/
        checkColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkColumn));
        checkColumn.setEditable(true);

        //初始化表中的其他数据列
        numColumn.setCellValueFactory(new PropertyValueFactory<StudentEntity,String>("num"));  //设置列值工程属性
        nameColumn.setCellValueFactory(new PropertyValueFactory<StudentEntity,String>("name"));
        deptColumn.setCellValueFactory(new PropertyValueFactory<StudentEntity,String>("dept"));
        majorColumn.setCellValueFactory(new PropertyValueFactory<StudentEntity,String>("major"));
        classNameColumn.setCellValueFactory(new PropertyValueFactory<StudentEntity,String>("className"));
        cardColumn.setCellValueFactory(new PropertyValueFactory<StudentEntity,String>("card"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<StudentEntity,String>("genderName"));
        birthdayColumn.setCellValueFactory(new PropertyValueFactory<StudentEntity,String>("birthday"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<StudentEntity,String>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<StudentEntity,String>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<StudentEntity, String>("address"));
        dataTableView.getSelectionModel().selectFirst();
        selectAll.setOnAction(evt -> {
            dataTableView.getItems().forEach(
                    item -> item.setSelect(selectAll.isSelected())
            );
            evt.consume();
        });

        dataTableView.getSelectionModel().selectFirst();

        //添加选择行的监听器，用于更新右边栏
        TableView.TableViewSelectionModel<StudentEntity> tsm = dataTableView.getSelectionModel();
        ObservableList<Integer> list = tsm.getSelectedIndices();
        list.addListener(this::onTableRowSelect);
        setTableViewData();

        //检查鼠标点击行元素时增加监听器选择或取消该行的复选框
        dataTableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                Node source = event.getPickResult().getIntersectedNode();
                // 检查点击的节点是否在 TableView 的行上
                if (isRowOrCell(source)) {
                    // 处理点击行的事件
                    StudentEntity form = dataTableView.getSelectionModel().getSelectedItem();
                    if(form != null) {
                        form.setSelect(!form.isSelect());
                    }
                }
            }
        });

        //设置表格的属性
        dataTableView.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
        );
        Styles.toggleStyleClass(dataTableView, Styles.BORDERED);//显示表格轮廓
        Styles.toggleStyleClass(dataTableView, Styles.STRIPED);//每行呈斑马交错颜色

    }

    /**
     * 辅助方法：检查节点是否是有效行或单元格
     */
    private boolean isRowOrCell(Node node) {
        if (node instanceof TableRow || node instanceof TableCell || node instanceof Text) {
            return true;
        }
        if (node.getParent() != null) {
            return isRowOrCell(node.getParent());
        }
        return false;
    }

    /**
     * 清除学生表单中输入信息
     */
    public void clearPanel(){
        studentId = null;
        numField.setText("");
        nameField.setText("");
        deptField.setText("");
        majorField.setText("");
        classNameField.setText("");
        cardField.setText("");
        genderComboBox.getSelectionModel().select(-1);
        birthdayPick.getEditor().setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
    }

    /**
     * 用于点击表格数据时，更新右边栏的内容
     */
    protected void changeStudentInfo() {
        StudentEntity form = dataTableView.getSelectionModel().getSelectedItem();
        if(form == null) {
            clearPanel();
            return;
        }
        studentId = form.getStudentId();
        DataRequest req = new DataRequest();
        req.put("studentId",studentId);
        DataResponse<StudentEntity> res = httpRequestUtil.requestObject("/api/student/getStudentInfo",req);
        if(res.getCode() != 0){
            MessageDialog.showDialog(res.getMsg());
            return;
        }
        form = res.getData();
        numField.setText(form.getNum());
        nameField.setText(form.getName());
        deptField.setText(form.getDept());
        majorField.setText(form.getMajor());
        classNameField.setText(form.getClassName());
        cardField.setText(form.getCard());
        genderComboBox.getSelectionModel().select(CommonMethod.getOptionItemIndexByValue(genderList, form.getGender()));
        birthdayPick.getEditor().setText(form.getBirthday());
        emailField.setText(form.getEmail());
        phoneField.setText(form.getPhone());
        addressField.setText(form.getAddress());
    }

    /**
     * 点击学生列表的某一行，根据studentId ,从后台查询学生的基本信息，切换学生的编辑信息
     */
    public void onTableRowSelect(ListChangeListener.Change<? extends Integer> change){
        changeStudentInfo();
    }

    /**
     * 点击查询按钮，从从后台根据输入的串，查询匹配的学生在学生列表中显示
     * 也是刷新界面的方法
     */
    @FXML
    protected void onQueryButtonClick() {
        String numName = numNameTextField.getText();
        DataRequest req = new DataRequest();
        DataResponse<ArrayList<StudentEntity>> res;
        //没有筛选值调用原来的接口，有筛选值调用新接口
        if (!filterCriteria.isEmpty()){
            //将筛选对象包装进请求
            req.putObject("filterCriteria",filterCriteria);
            res = httpRequestUtil.requestArrayList("/api/student/getStudentListByFilter/" + numName,req);

            //因为有筛选条件，修改一下筛选按钮
            hasFilter();

        }else {
            req.put("numName",numName);
            res = httpRequestUtil.requestArrayList("/api/student/getStudentList",req);
            resetFilter();
        }
        if(res != null && res.getCode()== 0) {
            studentList = res.getData();
            setTableViewData();
        }
    }

    /**
     *  添加新学生， 清空输入信息， 输入相关信息，点击保存即可添加新的学生
     */
    @FXML
    protected void onAddButtonClick() {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("add-student.fxml"));
        try {
            Parent root = fxmlLoader.load();
            AddStudentController controller = fxmlLoader.getController(); // 获取控制器对象

            // 创建一个新的 Stage 对象
            Stage addStage = new Stage();
            addStage.setTitle("添加学生信息");
            addStage.getIcons().add(MainApplication.icon);

            // 通过控制器的 setStage 方法传递 Stage 对象
            controller.setStage(addStage);

            // 设置 Scene 并显示 Stage
            Scene scene = new Scene(root, -1, -1);
            addStage.setScene(scene);

            // 添加关闭事件处理程序，用来关闭时自动刷新
            addStage.setOnHiding(event -> {
                onQueryButtonClick(); // 在关闭事件中调用 onQueryButtonClick() 方法
            });

            addStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 点击删除按钮 删除当前编辑的学生的数据
     */
    @FXML
    protected void onDeleteButtonClick() {
        getSelectedItem();//更新复选框选中的学生数据
        if (selectedItemList.isEmpty()){
            dialogUtil.openError("删除失败", "当前未选择任何元素，无法删除！");
            return;
        }
        //弹窗警告，若点击警告的“确定”，再继续进行删除操作
        dialogUtil.openWarning("警告", "将永久删除框选的 "+selectedItemList.size()+" 个学生的所有信息，并且无法还原，确认要删除吗?", this::deleteSelectedItems);
        /*StudentTableEntity form = dataTableView.getSelectionModel().getSelectedItem();
        if(form == null) {
            MessageDialog.showDialog("没有选择，不能删除");
            return;
        }
        int ret = MessageDialog.choiceDialog("确认要删除吗?");
        if(ret != MessageDialog.CHOICE_YES) {
            return;
        }
        studentId = form.getStudentId();
        DataRequest req = new DataRequest();
        req.put("studentId", studentId);
        DataResponse res = HttpRequestUtil.request("/api/student/studentDelete",req);
        if(res.getCode() == 0) {
            MessageDialog.showDialog("删除成功！");
            onQueryButtonClick();
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }*/
    }

    /**
     * 删除复选框选中的学生对象
     */
    public void deleteSelectedItems(){
        ArrayList<Integer> studentIdList = new ArrayList<Integer>();
        //提取复选框选中的学生的id，包装成列表传后端
        for (StudentEntity item:
             selectedItemList) {
            studentIdList.add(item.getStudentId());
        }
        DataRequest req = new DataRequest();
        req.put("studentId", studentIdList);
        DataResponse res = HttpRequestUtil.deleteRequest("/api/student/studentDeleteAll",req);
        if (res != null){
            if (res.getCode() == 0) {
                dialogUtil.openGeneric("删除成功", "删除成功！");
                onQueryButtonClick();
            } else {
                dialogUtil.openError("删除失败", res.getMsg());
            }
        }else {
            dialogUtil.openError("删除失败", "后台无响应，请稍后再尝试。");
        }
    }

    /**
     * 点击保存按钮，保存当前编辑的学生信息，如果是新添加的学生，后台添加学生
     */
    @FXML
    protected void onSaveButtonClick() {
        //弹窗错误
        if( numField.getText().equals("")) {
            dialogUtil.openError("修改失败", "学号为空，不能修改！");
            return;
        }
        if( !phoneField.getText().matches("^(\\d+)?$")) {
            dialogUtil.openError("添加失败", "电话格式不正确，不能添加！");
            return;
        }
        //将右边栏的输入内容包装为学生信息对象
        StudentEntity studentEntity =new StudentEntity();
        studentEntity.setNum(numField.getText());
        studentEntity.setName(nameField.getText());
        studentEntity.setDept(deptField.getText());
        studentEntity.setMajor(majorField.getText());
        studentEntity.setClassName(classNameField.getText());
        studentEntity.setCard(cardField.getText());
        if (genderComboBox.getSelectionModel() != null && genderComboBox.getSelectionModel().getSelectedItem() != null) {
            studentEntity.setGender(genderComboBox.getSelectionModel().getSelectedItem().getValue());
        }
        studentEntity.setBirthday(birthdayPick.getEditor().getText());
        if (!LocalDateUtil.isBeforeToday(studentEntity.getBirthday())){
            dialogUtil.openError("保存失败","出生日期无效！");
            return;
        }
        studentEntity.setEmail(emailField.getText());
        studentEntity.setPhone(phoneField.getText());
        studentEntity.setAddress(addressField.getText());

/*        Map form = studentEntity.toMap();
        System.out.println(form);*/
/*        Map form = new HashMap();
        form.put("num",numField.getText());
        form.put("name",nameField.getText());
        form.put("dept",deptField.getText());
        form.put("major",majorField.getText());
        form.put("className",classNameField.getText());
        form.put("card",cardField.getText());
        if(genderComboBox.getSelectionModel() != null && genderComboBox.getSelectionModel().getSelectedItem() != null)
            form.put("gender",genderComboBox.getSelectionModel().getSelectedItem().getValue());
        form.put("birthday",birthdayPick.getEditor().getText());
        form.put("email",emailField.getText());
        form.put("phone",phoneField.getText());
        form.put("address",addressField.getText());*/
        DataRequest req = new DataRequest();
        //将学生id单独包装方便后端
        req.put("studentId", studentId);
        //将新学生信息包装进请求
        req.putObject("form", studentEntity);
        DataResponse res = HttpRequestUtil.request("/api/student/studentEditSave",req);
        if(res.getCode() == 0) {
            studentId = CommonMethod.getIntegerFromObject(res.getData());
            dialogUtil.openGeneric("提交成功","提交成功！",null);
            onQueryButtonClick();
            // MessageDialog.showDialog("提交成功！");
        }
        else {
            MessageDialog.showDialog(res.getMsg());
        }
    }

    /**
     * doNew() doSave() doDelete() 重写 ToolController 中的方法， 实现选择 新建，保存，删除 对学生的增，删，改操作
     */
    public void doNew(){
        clearPanel();
    }
    public void doSave(){
        onSaveButtonClick();
    }
    public void doDelete(){
        onDeleteButtonClick();
    }

    /**
     * 导出学生信息表的示例 重写ToolController 中的doExport 这里给出了一个导出学生基本信息到Excl表的示例， 后台生成Excl文件数据，传回前台，前台将文件保存到本地
     */
    public void doExport(){
        String numName = numNameTextField.getText();
        DataRequest req = new DataRequest();
        req.put("numName",numName);
        byte[] bytes = HttpRequestUtil.requestByteData("/api/student/getStudentListExcl", req);
        if (bytes != null) {
            try {
                FileChooser fileDialog = new FileChooser();
                fileDialog.setTitle("请选择保存的文件");
                fileDialog.setInitialDirectory(new File("C:/"));
                fileDialog.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("XLSX 文件", "*.xlsx"));
                File file = fileDialog.showSaveDialog(null);
                if(file != null) {
                    FileOutputStream out = new FileOutputStream(file);
                    out.write(bytes);
                    out.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }

    /**
     * 获取复选框选中的列表对象
     * @return 复选框选中的对象列表
     */
    private List<StudentEntity> getSelectedItem(){
        List<StudentEntity> selectedItems = new ArrayList<StudentEntity>();
        for (StudentEntity items :
             observableList) {
            if (items.isSelect()){
                if (selectedItems != null) {
                    selectedItems.add(items);
                }
            }
        }
        selectedItemList = selectedItems;
        return  selectedItems;
    }

    /**
     * 重置筛选条件并修改按钮
     */
    private void resetFilter() {
        addFilterButton.setVisible(true);
        addFilterButton.setManaged(true);
        changeFilterButton.setManaged(false);
        changeFilterButton.setVisible(false);
        resetFilterButton.setVisible(false);
        resetFilterButton.setManaged(false); // 隐藏按钮并且不占用空间
        filterLabel.setText("筛选：当前无筛选条件");
        filterCriteria.empty();//清空筛选条件
    }

    /**
     * 检测到存在筛选条件时，修改筛选按钮
     */
    private void hasFilter(){
        addFilterButton.setVisible(false);
        addFilterButton.setManaged(false);
        changeFilterButton.setManaged(true);
        changeFilterButton.setVisible(true);
        resetFilterButton.setVisible(true);
        resetFilterButton.setManaged(true); // 隐藏按钮并且不占用空间
        filterLabel.setText("筛选：当前已设置筛选条件");
    }

    /**
     * 添加或修改筛选
     */
    private void setFilter(){
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("filter-student.fxml"));
        try {
            Parent root = fxmlLoader.load();
            FilterStudentController controller = fxmlLoader.getController(); // 获取控制器对象

            // 创建一个新的 Stage 对象
            Stage filterStage = new Stage();
            filterStage.setTitle("筛选条件");
            filterStage.getIcons().add(MainApplication.icon);

            // 设置 Scene 并显示 Stage
            Scene scene = new Scene(root, -1, -1);
            filterStage.setScene(scene);
            filterStage.show();
            // 初始化筛选控制器所需要的值，并把筛选条件的指针传进去，使在弹出页面更改的会自动同步到这个页面
            controller.init(filterStage, filterCriteria, this::onQueryButtonClick);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

/**
     * 点击添加筛选按钮
     * @param actionEvent .
     */
    public void onAddFilterButtonClicked(ActionEvent actionEvent) {
        setFilter();
    }

    /**
     * 点击修改筛选按钮
     * @param actionEvent .
     */
    public void onChangeFilterButtonClicked(ActionEvent actionEvent) {
        setFilter();
    }

    /**
     * 点击清除筛选按钮
     * @param actionEvent .
     */
    public void onResetFilterButtonClicked(ActionEvent actionEvent) {
        resetFilter();
        onQueryButtonClick();
    }

    public void onGetExcelButtonClicked(ActionEvent actionEvent) {
        Integer selectedNumber = getSelectedItem().size();
        if (selectedNumber == 0){
            dialogUtil.openError("导出失败", "未选中任何学生信息");
        }else {
            dialogUtil.openInfo("导出学生信息", "点击确认导出选中的 "+selectedNumber+" 条学生信息。", this::getExcel);
        }
    }

    public void getExcel(){
        DataRequest req = new DataRequest();
        req.putObjectList("selectedStudent",getSelectedItem());
        byte[] bytes = HttpRequestUtil.requestByteData("/api/student/getSelectedStudentListExcl", req);
        if (bytes != null) {
            try {
                FileChooser fileDialog = new FileChooser();
                fileDialog.setTitle("请选择保存的文件");
                fileDialog.setInitialDirectory(new File("C:/"));
                fileDialog.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("XLSX 文件", "*.xlsx"));
                fileDialog.setInitialFileName("student.xlsx");
                File file = fileDialog.showSaveDialog(null);
                if(file != null) {
                    FileOutputStream out = new FileOutputStream(file);
                    out.write(bytes);
                    out.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onImportExcelButtonClicked(ActionEvent actionEvent) {
        FileChooser fileDialog = new FileChooser();
        fileDialog.setTitle("导入学生表格");
        fileDialog.setInitialDirectory(new File("C:/"));
        fileDialog.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XLSX 文件", "*.xlsx"));
        File file = fileDialog.showOpenDialog(null);
        if (file != null){
            DataResponse res = HttpRequestUtil.importData("/api/student/importByExcel", file.getPath(), "");
            if (res != null) {
                if (res.getCode() == 0) {
                    dialogUtil.openGeneric("上传成功", "上传成功！");
                } else {
                    dialogUtil.openError("上传失败", res.getMsg());
                }
            } else {
                dialogUtil.openError("上传失败", "上传失败");
            }
            onQueryButtonClick();
        }
    }
}