/**
 * Created by Administrator on 2019/3/12/012.
 */
public class OOMTest {

  public static void main(String[] args) {
//    try {
//      //准备url
//      URL url = new File("D:\\github\\loan_github\\loan-service\\target\\test-classes").toURI().toURL();
//      URL[] urls = {url};
//      //获取有关类型加载的JMX接口
//      ClassLoadingMXBean loadingBean = ManagementFactory.getClassLoadingMXBean();
//      //用于缓存类加载器
//      List<ClassLoader> classLoaders = new ArrayList<ClassLoader>();
//      while (true) {
//        //加载类型并缓存类加载器实例
//        ClassLoader classLoader = new URLClassLoader(urls);
//        classLoaders.add(classLoader);
//        classLoader.loadClass("OOMTest");
//        //显示数量信息（共加载过的类型数目，当前还有效的类型数目，已经被卸载的类型数目）
//        System.out.println("total: " + loadingBean.getTotalLoadedClassCount());
//        System.out.println("active: " + loadingBean.getLoadedClassCount());
//        System.out.println("unloaded: " + loadingBean.getUnloadedClassCount());
//      }
//    } catch (Exception e) {
//      e.printStackTrace();
//    }

    Object aa = null;
    String bbb = aa + "_" + "1" + "_" + "2";
    System.out.println(bbb);
  }
}
