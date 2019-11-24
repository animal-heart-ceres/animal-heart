package com.animalheart.animalheart.controllers;

import com.animalheart.animalheart.models.Animal;
import com.animalheart.animalheart.models.Event;
import com.animalheart.animalheart.repositories.EventRepository;
import com.animalheart.animalheart.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class EventController {

    @Autowired
    UserRepository userDao;

    @Autowired
    EventRepository eventDao;

    @GetMapping("/create-event")
    public String showAnimalForm(Model vModel){
        vModel.addAttribute("event", new Event());
        return"/index";
    }

    @PostMapping("/create-event")
    public String createEvent(@ModelAttribute Event event){
//        set fk
        eventDao.save(event);
        return "redirect:/";
    }

    @GetMapping("/events/showAll")
    public String showAllEvents(Model vModel) {
        List<Event> eventList = eventDao.findAll();
        vModel.addAttribute("eventList", eventList);
        return "/view-events";
    }

    @GetMapping("/event-profile/{eventId}")
    public String showEvent(@PathVariable Long eventId, Model vModel){
        Event currentEvent = eventDao.getOne(eventId);
        vModel.addAttribute("event", currentEvent);
        return"/event-profile";
    }

    //Logic moved to view organization-profile controller. When I go to my profile, I expect to see all my events that I have created.

//    @GetMapping("/events/{userId}")
//    public String showUsersEvents(@PathVariable Long userId, Model vModel){
//        List<Event> allEvents = eventDao.findAll();
//        List<Event> usersEvents = new ArrayList<>();
//        for(Event event : allEvents) {
//            if(event.getUser().getId() == userId) {
//                usersEvents.add(event);
//            }
//        }
//        vModel.addAttribute("usersEvents", usersEvents);
//        return "/index";
//    }

    @PostMapping("/event/{eventId}/edit")
    public String editEvent(@PathVariable Long eventId, @RequestParam(name = "title") String title, @RequestParam(name = "description") String description, @RequestParam(name = "location") String location) {
        Event oldEvent = eventDao.getOne(eventId);
        oldEvent.setTitle(title);
        oldEvent.setDescription(description);
        oldEvent.setLocation(location);
        eventDao.save(oldEvent);
        return"redirect:/";
    }

    @PostMapping("/event/{eventId}/delete")
    public String deleteEvent(@PathVariable Long eventId) {
        Event currentEvent = eventDao.getOne(eventId);
        eventDao.delete(currentEvent);
        return "redirect:/";
    }

}
