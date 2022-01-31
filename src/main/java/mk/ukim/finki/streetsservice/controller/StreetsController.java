package mk.ukim.finki.streetsservice.controller;

import lombok.extern.slf4j.Slf4j;
import mk.ukim.finki.streetsservice.entity.Street;
import mk.ukim.finki.streetsservice.service.StreetService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/streets")
@Slf4j
public class StreetsController {

    private final StreetService streetService;

    public StreetsController(StreetService streetService) {
        this.streetService = streetService;
    }

    @GetMapping
    public String getShowAndAddStreetPage(@RequestParam(required = false) String error, Model model){

        if(error != null && error.isEmpty()){
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        model.addAttribute("showAllStreets", this.streetService.findAll());

        return "showAllStreets";
    }

    @GetMapping("/addNewStreet")
    public String addNewStreet(){

        return "addNewStreet";

    }

    @PostMapping("/add-street")
    public String addStreet(@RequestParam(required = false) Long id, HttpServletRequest request){

        String streetName = request.getParameter("streetName");
        Float streetLat = Float.parseFloat(request.getParameter("streetLat"));
        Float streetLong = Float.parseFloat(request.getParameter("streetLong"));

        if(id != null){
            this.streetService.editStreet(id, streetName, streetLat, streetLong);
        }
        else {
            this.streetService.addNewStreet(streetName, streetLat, streetLong);
        }

        return "redirect:/showAndAddStreet";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteStreet(@PathVariable Long id){

        this.streetService.deleteById(id);
        return "redirect:/showAndAddStreet";
    }

    @GetMapping("/edit-street/{id}")
    public String editStreet(@PathVariable Long id, Model model){

        if(this.streetService.findById(id).isPresent()){
            Street street = this.streetService.findById(id).get();
            model.addAttribute("selectedStreet", street);
            return "addNewStreet";
        }
        return "redirect:/showAndAddStreet?error=FuelNotFoundException";
    }


}
