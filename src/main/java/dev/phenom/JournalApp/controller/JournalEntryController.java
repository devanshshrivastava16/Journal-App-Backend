package dev.phenom.JournalApp.controller;
import dev.phenom.JournalApp.Entity.JournalEntry;
import dev.phenom.JournalApp.Entity.User;
import dev.phenom.JournalApp.Service.JournalEntryService;
import dev.phenom.JournalApp.Service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.getByUserName(userName);
        List<JournalEntry> all = user.getJournalEntryList();
        if(all !=null && !all.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(all);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    @PostMapping
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry entry) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            journalEntryService.saveEntry(entry,userName);
            return new ResponseEntity<>(entry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.getByUserName(userName);
        List<JournalEntry>  collect = user.getJournalEntryList().stream().filter(x->x.getId().equals(myId)).collect(Collectors.toList());
        if(!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry =  journalEntryService.findbyId(myId);
            if(journalEntry.isPresent()){
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{myId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean removed=journalEntryService.deleteEntryById(myId,userName);
        if (removed) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/id/{myId}")
    public ResponseEntity<?> updateJournalById(@PathVariable ObjectId myId, @RequestBody JournalEntry entry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.getByUserName(userName);
        List<JournalEntry>  collect = user.getJournalEntryList().stream().filter(x->x.getId().equals(myId)).collect(Collectors.toList());
        if(!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry =  journalEntryService.findbyId(myId);
            if(journalEntry.isPresent()){
                JournalEntry oldEntry = journalEntry.get();
                oldEntry.setJournalTitle(entry.getJournalTitle() != null && !entry.getJournalTitle().isEmpty() ? entry.getJournalTitle() : oldEntry.getJournalTitle());
                oldEntry.setJournalContent(entry.getJournalContent() != null && !entry.getJournalContent().isEmpty() ? entry.getJournalContent() : oldEntry.getJournalContent());
                journalEntryService.saveEntry(oldEntry);
                return new ResponseEntity<>(oldEntry, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
}