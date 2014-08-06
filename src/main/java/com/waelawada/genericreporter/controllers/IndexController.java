package com.waelawada.genericreporter.controllers;

import com.waelawada.genericreporter.domain.Procedure;
import com.waelawada.genericreporter.utils.ProcedureUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This
 *
 * @author: Wael Awada
 */
@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    private DriverManagerDataSource dataSource;

    @RequestMapping("index")
    public String getIndexView(Model model){
        List<Procedure> procedures = getProcedures();

        boolean listIsNull = (procedures == null);

        model.addAttribute("listIsNull",listIsNull);
        model.addAttribute("procedures",procedures);

        return "index";
    }

    @RequestMapping("/")
    public String getIndexView2(Model model){
        return getIndexView(model);
    }

    private List<Procedure> getProcedures(){

        List<Procedure> procedures = new ArrayList<Procedure>();

        try {
            PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement("select db,name from mysql.proc");
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Procedure procedure = new Procedure();
                String procedureDb = resultSet.getString("db");
                String procedureName = resultSet.getString("name");
                procedure.setName(procedureName);
                procedure.setDatabase(procedureDb);
                procedure.setParameters(ProcedureUtils.getFunctionParameterListAsMap(procedureDb,procedureName,dataSource));
                procedures.add(procedure);
            }
        } catch (Exception e) {
            procedures = null;
        }
        return procedures;
    }

}
