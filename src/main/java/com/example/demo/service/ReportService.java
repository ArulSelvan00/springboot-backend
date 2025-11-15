package com.example.demo.service;

import com.example.demo.model.DeliveredOrder;
import com.example.demo.repository.DeliveredOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private DeliveredOrderRepository deliveredOrderRepository;

    public byte[] generateMonthlyDeliveredOrdersCsv(int year, int month) {

        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1, 0, 0, 0);
        java.util.Date startDate = cal.getTime();

        cal.set(year, month - 1, cal.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
        java.util.Date endDate = cal.getTime();

        List<DeliveredOrder> orders = deliveredOrderRepository.findByDeliveredDateBetween(startDate, endDate);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PrintWriter writer = new PrintWriter(baos)) {

            writer.println("Order ID,Product Name,Customer Name,Total Price,Contact,Address,Village,Pincode,Delivered Date");

            for (DeliveredOrder order : orders) {
                String line = String.format("%d,\"%s\",\"%s\",%.2f,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                        order.getId(),
                        order.getProductName().replace("\"", "\"\""),
                        order.getCustomerName().replace("\"", "\"\""),
                        order.getTotalPrice(),
                        order.getContact(),
                        order.getAddress().replace("\"", "\"\""),
                        order.getVillage().replace("\"", "\"\""),
                        order.getPincode(),
                        order.getDeliveredDate() != null ? order.getDeliveredDate().toString() : ""
                );
                writer.println(line);
            }

            writer.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate CSV report: " + e.getMessage(), e);
        }
    }
}