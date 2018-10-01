package com.invoister.cucumber.stepdefs;

import com.invoister.InvoisterApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = InvoisterApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
