package ua.sgkhmja.wboard.cucumber.stepdefs;

import ua.sgkhmja.wboard.WBoardApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = WBoardApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
