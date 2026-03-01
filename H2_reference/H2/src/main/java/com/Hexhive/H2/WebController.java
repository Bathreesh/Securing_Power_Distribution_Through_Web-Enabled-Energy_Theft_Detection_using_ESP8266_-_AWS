package com.Hexhive.H2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * WEB CONTROLLER (The Traffic Police)
 * 
 * This class handles incoming HTTP requests (like clicking a link or submitting
 * a form)
 * and decides what HTML page to show or what data to save.
 */
@Controller
public class WebController {

    // Automatically inject our Repository to interact with the database
    @Autowired
    private ValueRepository valueRepo;

    /**
     * HELPER METHOD:
     * This checks if we have data in the database.
     * If not, it creates a default record starting with the value 1.
     */
    private ValueEntity getOrCreateValue() {
        var all = valueRepo.findAll();
        if (all.isEmpty()) {
            ValueEntity v = new ValueEntity();
            v.setCurrentValue(1);
            return valueRepo.save(v);
        }
        return all.get(0); // For this app, we only care about the first record
    }

    /**
     * PAGE: View Value
     * Triggered by browsing to: http://localhost:8080/value
     */
    @GetMapping("/value")
    public String showValue(Model model) {
        ValueEntity v = getOrCreateValue();
        // 'model' is used to send data to the HTML template (display.html)
        model.addAttribute("value", v.getCurrentValue());
        return "display"; // Look for a file named "display.html" in /templates
    }

    /**
     * PAGE: Update Form
     * Triggered by browsing to: http://localhost:8080/update
     */
    @GetMapping("/update")
    public String showUpdateForm(Model model) {
        ValueEntity v = getOrCreateValue();
        model.addAttribute("current", v.getCurrentValue());
        return "update"; // Look for "update.html"
    }

    /**
     * ACTION: Process the Form Submission
     * Triggered when someone clicks "Update" on the/update page.
     */
    @PostMapping("/update")
    public String updateValue(@RequestParam Integer newValue) {
        ValueEntity v = getOrCreateValue();
        v.setCurrentValue(newValue); // Set the entity's value to the new one from the form
        valueRepo.save(v); // Save the changes back to H2 database

        // After updating, tell the browser to go back to the value display page
        return "redirect:/value";
    }
}