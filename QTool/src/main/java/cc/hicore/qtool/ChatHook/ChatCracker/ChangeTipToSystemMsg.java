package cc.hicore.qtool.ChatHook.ChatCracker;

import cc.hicore.HookItemLoader.Annotations.MethodScanner;
import cc.hicore.HookItemLoader.Annotations.UIItem;
import cc.hicore.HookItemLoader.Annotations.VerController;
import cc.hicore.HookItemLoader.Annotations.XPExecutor;
import cc.hicore.HookItemLoader.Annotations.XPItem;
import cc.hicore.HookItemLoader.bridge.BaseXPExecutor;
import cc.hicore.HookItemLoader.bridge.MethodContainer;
import cc.hicore.HookItemLoader.bridge.MethodFinderBuilder;
import cc.hicore.HookItemLoader.bridge.QQVersion;
import cc.hicore.HookItemLoader.bridge.UIInfo;
import cc.hicore.ReflectUtils.MClass;
import cc.hicore.ReflectUtils.MField;
import cc.hicore.ReflectUtils.MMethod;

@XPItem(name = "状态消息隐藏",itemType = XPItem.ITEM_Hook)
public class ChangeTipToSystemMsg{
    @UIItem
    @VerController
    public UIInfo getUIInfo(){
        UIInfo ui = new UIInfo();
        ui.groupName = "聊天净化";
        ui.name = "状态消息隐藏";
        ui.desc = "阻止状态消息显示在自己的消息记录中";
        ui.targetID = 2;
        ui.type = 1;
        return ui;
    }
    @MethodScanner
    @VerController(max_targetVer = QQVersion.QQ_8_9_0)
    public void getHookMethod(MethodContainer container ){
        container.addMethod("hook",MMethod.FindMethod("com.tencent.mobileqq.troop.data.TroopAndDiscMsgProxy",null, void.class,new Class[]{
                String.class,
                int.class,
                MClass.loadClass("com.tencent.mobileqq.data.MessageRecord"),
                boolean.class
        }));
    }
    @MethodScanner
    @VerController(targetVer = QQVersion.QQ_8_9_0)
    public void getHookMethod_890(MethodContainer container){
        container.addMethod(MethodFinderBuilder.newFinderByString("hook","insertToList ", m -> m.getDeclaringClass().getName().startsWith("com.tencent.imcore.message")));
    }
    @VerController
    @XPExecutor(methodID = "hook")
    public BaseXPExecutor hookWorker(){
        return param -> {
            Object ChatMsg = param.args[2];
            if (ChatMsg.getClass().getName().contains("MessageForUniteGrayTip")){
                try {
                    MField.SetField(ChatMsg,"senderuin","10000");
                    MMethod.CallMethod(ChatMsg,"prewrite",void.class,new Class[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
