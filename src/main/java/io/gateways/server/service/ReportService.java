package io.gateways.server.service;

import io.gateways.server.model.Server;
import io.gateways.server.repo.ServerRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {
    @Autowired
    private ServerRepository serverRepository;
    public String exportReport(String reportFormat, List<Server> servers) throws FileNotFoundException, JRException {
        String path= "D:\\All programming TEXT NOTE OF MINE\\Muntakim vai crud\\report";

        //load file and compile it
        File file = ResourceUtils.getFile("classpath:server.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(servers);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CreatedBy","Md. Al Shariar");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);
        if(reportFormat.equalsIgnoreCase("html")){
            JasperExportManager.exportReportToHtmlFile(jasperPrint,path+"\\servers.html");
        }
        if(reportFormat.equalsIgnoreCase("pdf")){
            JasperExportManager.exportReportToPdfFile(jasperPrint,path+"\\servers.pdf");
        }
        if(reportFormat.equalsIgnoreCase("csv")){
            JasperExportManager.exportReportToPdfFile(jasperPrint,path+"\\servers.xlsx");
        }
        return  "report generated in this path: "+path;
    }
}
