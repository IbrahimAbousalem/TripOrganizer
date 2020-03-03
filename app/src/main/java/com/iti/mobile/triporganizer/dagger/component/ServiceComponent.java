package com.iti.mobile.triporganizer.dagger.component;

import com.iti.mobile.triporganizer.appHead.ChatHeadService;
import com.iti.mobile.triporganizer.dagger.module.controller.ChatHeadServiceControllerModule;

import dagger.Subcomponent;

@Subcomponent(modules = {ChatHeadServiceControllerModule.class})

public interface ServiceComponent {
    void inject(ChatHeadService chatHeadService);

}
