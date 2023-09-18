package org.shop.app.suite;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.runner.RunWith;

@RunWith(org.junit.runners.Suite.class)
@SelectPackages({"org.shop.app.controller"})
@Suite
public class IntegrationSuite {
}
