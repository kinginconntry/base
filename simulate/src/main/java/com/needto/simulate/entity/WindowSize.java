package com.needto.simulate.entity;

import org.openqa.selenium.Dimension;

/**
 * @author Administrator
 */

public enum WindowSize {

    WEB_1920(){
        @Override
        public Dimension getDimension() {
            return new Dimension(1920, 1024);
        }
    },
    WEB_1350(){
        @Override
        public Dimension getDimension() {
            return new Dimension(1350, 1024);
        }
    },
    WEB_960(){
        @Override
        public Dimension getDimension() {
            return new Dimension(960, 1024);
        }
    },
    WEB_768(){
        @Override
        public Dimension getDimension() {
            return new Dimension(768, 1024);
        }
    },
    MOBILE_360(){
        @Override
        public Dimension getDimension() {
            return new Dimension(360, 640);
        }
    },
    MOBILE_320(){
        @Override
        public Dimension getDimension() {
            return new Dimension(320, 568);
        }
    },
    MOBILE_375(){
        @Override
        public Dimension getDimension() {
            return new Dimension(375, 667);
        }
    },
    MOBILE_768(){
        @Override
        public Dimension getDimension() {
            return new Dimension(768, 1024);
        }
    };
    public abstract Dimension getDimension();
}
