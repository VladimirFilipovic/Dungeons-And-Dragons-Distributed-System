package dnd.microservices.core.api.composite;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface CharacterCompositeService {

    @ApiOperation(
            value = "${api.character-composite.get-character-data.description}",
            notes = "${api.character-composite.get-character-data.notes}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request, invalid format of the request. See response message for more information."),
            @ApiResponse(code = 404, message = "Not found, the specified id does not exist."),
            @ApiResponse(code = 422, message = "Unprocessable entity, input parameters caused the processing to fail. See response message for more information.")
    })
    @GetMapping(
            value = "/full-character-info/{characterName}",
            produces = "application/json"
    )
    public CharacterComposite getCharacterData(@PathVariable String characterName);
}
