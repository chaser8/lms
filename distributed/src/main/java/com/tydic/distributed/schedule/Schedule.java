package com.tydic.distributed.schedule;

public class Schedule {
	
	/**
	 * @Title: ScheduleJob
	 * @Description:  
	 * @author yzb yangzb@tydic.com,yzhengbin@gmail.com
	 * @date 2017年4月19日 下午5:55:27
	 */
	public Schedule(ScheduleCallBack callBack) {
		this.callBack=callBack;
	}

    private String scheduleJobKey;
    private String scheduleJobName;//任务名
    private String scheduleJobGroup;//任务组
    private String scheduleJobState;
    private String cronExpr;//cron表达式（执行时机）
    private String desc;//任务描述
    private ScheduleCallBack callBack;

    private boolean hasState;//是否有状态(如果有状态，两次调度执行必须等待上次执行完！)
    private String beanClass;
    private ClassLoader classLoader = this.getClass().getClassLoader();
    private String methodName;
    private Object arg;//参数
    
    public void callBack(Object obj){
    	callBack.scheduleReturn(obj,scheduleJobName);
    }
    
    public ClassLoader getClassLoader() {
		return classLoader;
	}

	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public static String buildKey(String scheduleJobGroup,String scheduleJobName){
        return scheduleJobGroup+"&"+scheduleJobName;
    }

    public String getScheduleJobKey() {
        if(scheduleJobKey==null){
            scheduleJobKey = buildKey(scheduleJobGroup,scheduleJobName);
        }
        return scheduleJobKey;
    }

    public String getScheduleJobName() {
        return scheduleJobName;
    }

    public void setScheduleJobName(String scheduleJobName) {
        this.scheduleJobName = scheduleJobName;
        this.scheduleJobKey = null;
    }

    public String getScheduleJobGroup() {
        return scheduleJobGroup;
 }

    public void setScheduleJobGroup(String scheduleJobGroup) {
        this.scheduleJobGroup = scheduleJobGroup;
        this.scheduleJobKey = null;
    }

    public String getScheduleJobState() {
        return scheduleJobState;
    }

    protected void setScheduleJobState(String scheduleJobState) {
        this.scheduleJobState = scheduleJobState;
    }

    public String getCronExpr() {
        return cronExpr;
    }

    public void setCronExpr(String cronExpr) {
        this.cronExpr = cronExpr;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean hasState() {
        return hasState;
    }

    public void setHasState(boolean hasState) {
        this.hasState = hasState;
    }

    public String getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(String beanClass) {
        this.beanClass = beanClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object getArg() {
        return arg;
    }

    public void setArg(Object arg) {
        this.arg = arg;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj!=null && obj instanceof Schedule){
            Schedule o = (Schedule)obj;
            boolean ret = getScheduleJobKey().equals(o.getScheduleJobKey())
                    && hasState==o.hasState()
                    && beanClass.equals(o.getBeanClass())
                    && methodName.equals(o.getMethodName())
                    && ((arg!=null && arg.equals(o.getArg())) || (arg==null && o.getArg()==null));
            if(ret){
                String con1 = cronExpr.substring(cronExpr.indexOf(" "));
                String con2 = o.getCronExpr().substring(o.getCronExpr().indexOf(" "));
                return con1.equals(con2);
            }else{
                return false;
            }
        }
        return false;
    }

    @Override
    public String toString(){
        return getScheduleJobKey()+"|"+getCronExpr()+"|"+hasState()+"|"+getBeanClass()+"|"+getMethodName()+"|"+arg;
    }
    public interface ScheduleCallBack{
    	public void scheduleReturn(Object obj,String scheduleJobName);
    }
}
