namespace UsuariosApi.DTOs;

public class CrearUsuarioDto
{
    public string Nombre { get; set; } = string.Empty;

    public string Apellido { get; set; } = string.Empty;

    public string Correo { get; set; } = string.Empty;

    public string Password { get; set; } = string.Empty;

    public DateTime FechaNacimiento { get; set; }

    public string Universidad { get; set; } = string.Empty;

    public int Semestre { get; set; }
}