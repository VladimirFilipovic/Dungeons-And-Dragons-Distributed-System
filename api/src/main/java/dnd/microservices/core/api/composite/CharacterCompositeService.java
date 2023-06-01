package dnd.microservices.core.api.composite;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

public interface CharacterCompositeService {

        @ApiOperation(value = "${api.character-composite.get-character-data.description}", notes = "${api.character-composite.get-character-data.notes}")
        @ApiResponses(value = {
                        @ApiResponse(code = 400, message = "Bad Request, invalid format of the request. See response message for more information."),
                        @ApiResponse(code = 404, message = "Not found, the specified id does not exist."),
                        @ApiResponse(code = 422, message = "Unprocessable entity, input parameters caused the processing to fail. See response message for more information.")
        })
        @GetMapping(value = "/characters/{characterId}", produces = "application/json")
        public CharacterComposite getCharacterData(@PathVariable String characterId);

        @ApiOperation(value = "${api.character-composite.create-composite-character.description}", notes = "${api.character-composite.create-composite-character.notes}")
        @ApiResponses(value = {
                        @ApiResponse(code = 400, message = "Bad Request, invalid format of the request. See response message for more information."),
                        @ApiResponse(code = 422, message = "Unprocessable entity, input parameters caused the processing to fail. See response message for more information.")
        })
        @PostMapping(value = "/characters", consumes = "application/json", produces = "application/json")
        public void createCharacter(CharacterComposite characterComposite);

        @ApiOperation(value = "${api.character-composite.delete-composite-character.description}", notes = "${api.character-composite.delete-composite-character.notes}")
        @ApiResponses(value = {
                        @ApiResponse(code = 400, message = "Bad Request, invalid format of the request. See response message for more information."),
                        @ApiResponse(code = 404, message = "Not found, the specified id does not exist."),
                        @ApiResponse(code = 422, message = "Unprocessable entity, input parameters caused the processing to fail. See response message for more information.")
        })
        @DeleteMapping(value = "/character-composite/{productId}")
        void deleteCompositeCharacter(@PathVariable String character);
}
