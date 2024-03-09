package com.example.demo.util;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.example.demo.models.LibroRequest;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ReportGenerator {

	public byte[] exportToPdf(List<LibroRequest> list) throws JRException, FileNotFoundException {
		return JasperExportManager.exportReportToPdf(getReport(list));
	}

	private JasperPrint getReport(List<LibroRequest> list) throws FileNotFoundException, JRException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("libroData", new JRBeanCollectionDataSource(list));

		JasperPrint report = JasperFillManager.fillReport(
				JasperCompileManager
						.compileReport(ResourceUtils.getFile("classpath:LaboratorioFinalLPll.jrxml").getAbsolutePath()),
				params, new JREmptyDataSource());

		return report;
	}

}
