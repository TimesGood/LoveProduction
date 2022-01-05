 # 结构：
 |———assets：存放静态资源  
 |———filters  
 |   |———debug：debug环境配置文件  
 |   |———release：release环境配置文件  
 |   |———sign：打包签名文件  
 |———java  
 |   |———com.aige.loveproduction  
 |   |   |———action：意图接口  
 |   |   |———adapter：列表适配器编写  
 |   |   |———animation：代码动画  
 |   |   |———base：封装基础业务类  
 |   |   |———bean：实体对象  
 |   |   |———listener：监听  
 |   |   |———manager：管理类  
 |   |   |———mvp：mvp结构业务核心  
 |   |   |———net：网络请求封装  
 |   |   |———permission：权限获取封装  
 |   |   |———util：工具类  
 |———jniLibs：so文件  
 |———res：资源文件  
# action
处理每个页面可能都会使用到的一些操作的封装  

# customui
自定义一些组件，如果需要自定义组件，请在该目录下创建自定义  

# adapter
业务需要，配置适配器，处理不同形式的数据，如需增加适配器，请继承AppAdapter，其中AppAdapter继承于BaseAdapter  
BaseAdapter：ListView有内置的一些监听事件，但是RecycleView却没有，所以在BaseAdapter中主要对监听事件进行了一些封装，  
当然也统一了一些数据绑定的入口onBindView  
AppAdapter：主要对要适配的数据进行处理，把RecycleView.Adapter<?>原来比较繁琐配置适配器数据进行统一，  
增加对外添加、修改、删除、插入等对数据条目的处理  
# mvp，增加业务
1、当你需要增加一个页面，并且该页面需要请求网络获取数据时，你需要在mvp/contract之下创建一个契约类，该类之下需创建三个接口  
例：  
public interface DemoContract {  
    interface Model extends IBaseModel {  
        //获取数据  
        Observable<BaseBean<?>> getXXX(String param);  
    }  
    interface View extends IBaseView {  
        //网络请求成功的数据返回  
        void onXXXSuccess(List<?> data);  
    }  
    interface Presenter extends IBasePresenter<View> {  
        //当要调用接口获取数据时，实际调用的接口，也是网络请求逻辑的处理  
        void getXXX(String param);  
    }  
}  
2、在APIService之下添加要请求的接口，并配置一些参数  
@GET("/api/Paperless/Folder/GetMPRByBarCode")  
Observable<BaseBean<?>> getXXX(@Query("BarCode") String param);  
3、创建Model实例实现DemoContract中的Model  
public class DemoModel implements DemoContract.Model {  
    @Override  
    public Observable<BaseBean<?>> getXXX(String param) {  
        return getApi().getXXX(param);  
    }  
}  
4、创建Presenter实例实现DemoContract中的Presenter，并继承BasePresenter<DemoContract.View, DemoModel>  
public class DemoPresenter extends BasePresenter<DemoContract.View, DemoModel> implements DemoContract.Presenter{  
    @Override  
    public ApplyModel bindModel() {  
        return new ApplyModel();  
    }  
    @Override  
    public void getXXX(String param) {  
        checkViewAttached();  
        mModel.getMPRByBatchNo(param).compose(RxScheduler.Obs_io_main())  
                .to(mView.bindAutoDispose())  
                .subscribe(new BaseObserver<?>(){  
                    @Override  
                    public void onStart(Disposable d) {  
                        setDisposable(d);  
                        mView.showLoading();  
                    }  
                    @Override  
                    public void onSuccess(? data) {  
                        mView.onXXXSuccess(data);  
                    }  
                    @Override  
                    public void onError(String message) {  
                        mView.hideLoading();  
                        mView.onError(message);  
                    }  
                    @Override  
                    public void onNormalEnd() {  
                        mView.hideLoading()  
                    }  
                });  
    }  
}  
5、最后在Activity中把默认继承AppCompActivity的改为继承BaseActivity<DemoPresenter, DemoContract.View>并实现DemoContract.View  
把该写的东西写上，需要进行网络请求的地方调用mPresenter.getXXX(param)即可，接收请求回来的数据就在onXXXSuccess(data)中获取  
