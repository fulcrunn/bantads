export interface LoginResponse {
  access_token: string;
  token_type: string;
  tipo: string; 
  user: {
    id: string;
    login: string;
  };
}