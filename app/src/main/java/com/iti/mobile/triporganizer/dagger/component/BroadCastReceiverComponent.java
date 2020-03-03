package com.iti.mobile.triporganizer.dagger.component;

import com.iti.mobile.triporganizer.alarm.AlarmBroadCastReceiver;
import com.iti.mobile.triporganizer.dagger.module.controller.BroadCastReceiverModule;

import dagger.Subcomponent;

@Subcomponent(modules = {BroadCastReceiverModule.class})
public interface BroadCastReceiverComponent {
    void inject(AlarmBroadCastReceiver receiver);
}
